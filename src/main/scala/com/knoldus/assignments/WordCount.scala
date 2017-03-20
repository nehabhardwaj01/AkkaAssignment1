package com.knoldus.assignments

import akka.actor.{Props, ActorSystem, ActorRef, Actor}
import akka.util.Timeout
import scala.io.Source
import akka.pattern.ask
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

class WordCountPerLine extends Actor{

  def receive = {
    case msg : String => val wordsArray = msg.split(" ")
      sender() ! wordsArray.size }
}

class WordCount(ref : ActorRef) extends Actor{
  var count =0
  def receive = {
    case msg: String => {
      println(s"filename : $msg")

      for (line <- Source.fromFile(msg).getLines()) {
        implicit val timeout = Timeout(100 seconds)
        val l : Future[Int] = ( ref ? line ).mapTo[Int]
        Thread.sleep(100)
        l.foreach(x => count += x)
      }
      sender() ! count
    }
  }

}

object WordCount extends App{

  val system = ActorSystem("WordCountSystem")
  val props = Props[WordCountPerLine]
  val countPerLineRef = system.actorOf(props)
  val countRef = system.actorOf(Props(classOf[WordCount],countPerLineRef))
  val filename = "/home/neha/abc.txt"

  implicit val timeout = Timeout(10 seconds)
  val reply = countRef ? filename

  Thread.sleep(1000)

  print("Total Word Count is : ")
  reply.foreach(println _)
}
