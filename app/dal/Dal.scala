package dal

import modules.User
import slick.jdbc.MySQLProfile.api._
import utils.Globals

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object Dal {


  /**
    * thie method gets all rows in following series: 1, 1 + filterNum, 1 + filterNum + filterNum, ..., N
    * @param filterNum: the skipping step
    * @return
    */
  def getFilteredRows(filterNum: Int)(implicit exec: ExecutionContext): List[User] = {

    // declare the sql query
    val sql = sql"""
              SELECT first_name, last_name, age
              FROM (
                SELECT @row := @row +1 AS rownum, first_name, last_name, age
                FROM (SELECT @row :=0) r, users
              ) ranked
              WHERE rownum % ${filterNum} = 1 """.as[(String, String, Int)]


      // get the filtered rows from the users table
      val rows = {
        Await.result(Globals.DB.run(sql), Duration.Inf)
      }

      // create the users list by transform each row to a user
      val usersList = rows.map(x => User(x._1, x._2, x._3)).toList

      usersList
  }
}
