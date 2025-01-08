package fallatol.prototype

import cats.MonadThrow
import cats.effect.{Async, Ref}
import cats.implicits._
import fs2.Chunk
import io.circe.Codec
import fs2.io.file.{Files, Flag, Flags, Path, WalkOptions}
import io.circe.jawn.decode
import io.circe.syntax.EncoderOps

trait FileKey[K] {
  def toFileName(key: K): String
  def fromString[F[_]: MonadThrow](keyString: String): F[K]
  def fromFileName[F[_]: MonadThrow](fileName: String): F[K] = {
    val parts = fileName.split('.')

    if (parts.length == 2 && parts(1) == "json") MonadThrow[F].pure(parts(0)).flatMap(fromString[F])
    else MonadThrow[F].raiseError(new IllegalArgumentException(s"Unexpected filename: $fileName"))
  }
}

object FileKey {
  implicit val stringFileKey: FileKey[String] = new FileKey[String] {
    override def toFileName(key: String): String = s"$key.json"
    override def fromString[F[_]: MonadThrow](keyString: String): F[String] = MonadThrow[F].pure(keyString)
  }

  implicit val longFileKey: FileKey[Long] = new FileKey[Long] {
    override def toFileName(key: Long): String = s"${key.toString}.json"

    override def fromString[F[_]: MonadThrow](keyString: String): F[Long] =
      try {
        MonadThrow[F].pure(keyString.toLong)
      } catch {
        case nfe: NumberFormatException => MonadThrow[F].raiseError(nfe)
      }
  }

  def apply[K: FileKey]: FileKey[K] = implicitly[FileKey[K]]
}

trait PrototypeStorage[F[_], K, V] {
  def read(key: K): F[Option[V]]
  def write(key: K, value: V): F[Unit]
  def find(f: V => Boolean): F[Option[V]]
  def filter(f: V => Boolean): F[List[V]]
  def getAll: F[List[V]]
}

class FilePrototypeStorage[F[_]: Async, K, V: Codec](storagePath: Path, ref: Ref[F, Map[K, V]]) extends PrototypeStorage[F, K, V] {
  override def read(key: K): F[Option[V]] = ref.get.map(_.get(key))
  override def write(key: K, value: V): F[Unit] =
    writeValue(key, value) *> ref.update(_ + (key -> value))
  override def find(f: V => Boolean): F[Option[V]] = ref.get.map(_.values.find(f))
  override def filter(f: V => Boolean): F[List[V]] = getAll.map(_.filter(f))
  override def getAll: F[List[V]] = ref.get.map(_.values.toList)

  private def writeValue(key: K, value: V): F[Unit] =
    for {
      _ <- Async[F].ifM(Files.forAsync[F].isDirectory(storagePath))(
        Async[F].unit, Async[F].raiseError(new RuntimeException(s"${storagePath.toString} is not a directory.")))
      filePath = storagePath / s"$key.json"
      _ <- Files.forAsync[F].deleteIfExists(filePath)
      valueJson = value.asJson.spaces2
      _ <- Files.forAsync[F].createFile(filePath)
      _ <- Files.forAsync[F].open(filePath, Flags(Flag.Write)).use(_.write(Chunk.array(valueJson.getBytes), 0))
    } yield ()
}

object FilePrototypeStorage {
  def make[F[_]: Async, K: FileKey, V: Codec](storageRoot: Path, tableName: String): F[FilePrototypeStorage[F, K, V]] =
    for {
      path <- ensureDirectory(storageRoot, tableName)
      map <- loadAll[F, K, V](path)
      ref <- Ref[F].of(map)
    } yield new FilePrototypeStorage[F, K, V](path, ref)

  private def ensureDirectory[F[_]: Async](storageRoot: Path, tableName: String): F[Path] = {
    val path = storageRoot / tableName
    for {
      _ <- Async[F].ifM(Files.forAsync[F].exists(path))(
        Files.forAsync[F].isRegularFile(path).flatMap(plainFile =>
          Async[F].raiseWhen(plainFile)(new RuntimeException(s"File $path is not a directory."))
        ),
        Files.forAsync[F].createDirectory(path))
    } yield path
  }

  private def loadAll[F[_]: Async, K: FileKey, V: Codec](path: Path): F[Map[K, V]] =
    Files.forAsync[F].walkWithAttributes(path, WalkOptions.Default.withMaxDepth(1))
      .filter(pi => pi.attributes.isRegularFile && pi.path.extName == ".json")
      .evalMap(pi => {
        for {
          content <- Files.forAsync[F].readUtf8(pi.path).compile.toList.map(_.mkString("\n"))
          value <- Async[F].fromEither(decode[V](content))
          key <- FileKey[K].fromFileName[F](pi.path.fileName.toString)
        } yield key -> value
      }).compile.toList.map(_.toMap)
}
