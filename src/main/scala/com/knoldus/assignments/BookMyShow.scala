package com.knoldus.assignments

import akka.actor.{Actor, Props, ActorSystem}


case class BookSeat(seats : Int)
case class CancelSeat(seats : Int)


class BookMyShow extends Actor{

  val numberOfSeats = List[Int](100)
  println(numberOfSeats)

  def receive = {

    case msg : BookSeat => {
      val seats = msg.seats
      println("Seats Booked")
      //numberOfSeats.length = numberOfSeats.length - seats
      println(s"New Size : ${numberOfSeats.size}")
    }

    case msg: CancelSeat => {
      val seats = msg.seats
      println("Seats Cancelled")
      //numberOfSeats.size += seats
    }

  }
}

object BookMyShow extends App{

  val system = ActorSystem("BookMyShow")
  val props = Props[BookMyShow]
  val ref = system.actorOf(props)

  ref ! BookSeat(2)

}
