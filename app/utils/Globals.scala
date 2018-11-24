package utils

import slick.jdbc.MySQLProfile.api._

object Globals {
  // access to the the database
  val DB = Database.forConfig("db")
}
