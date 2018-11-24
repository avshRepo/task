package controllers

import akka.actor.ActorSystem
import javax.inject._
import modules.User
import play.api.libs.json.Json
import play.api.mvc._
import services.ServicesList

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param cc standard controller components
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.  When rendering content, you should use Play's
 * default execution context, which is dependency injected.  If you are
 * using blocking operations, such as database or network access, then you should
 * use a different custom execution context that has a thread pool configured for
 * a blocking API.
 */
@Singleton
class AsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  /**
    * returns hello when entering the page
    * @return
    */
  def index = Action {
    Ok("hello")
  }

  /**
    * in case filterNum is positive Int :returns all the rows except the ones we need to skip according to the filterNum parameter
    * else it returns error message
    * @param filterNum: the skipping step
    */
  def getFilteredRows(filterNum: Int): Action[AnyContent] =
          Action.async {
              // get the usersList from the users table
              val rows: Future[List[User]] = Future(ServicesList.getFilteredRowsService(filterNum))

              // define the user scheme for json
              implicit val userFormat = Json.format[User]

              rows.map((usersList: List[User]) => Ok(Json.obj("users" -> usersList)))
            }
}
