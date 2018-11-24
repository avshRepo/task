package services

import dal.Dal
import modules.User

import scala.concurrent.ExecutionContext

object ServicesList {
  def getFilteredRowsService(filterNum: Int)(implicit exec: ExecutionContext): List[User] = Dal.getFilteredRows(filterNum)
}
