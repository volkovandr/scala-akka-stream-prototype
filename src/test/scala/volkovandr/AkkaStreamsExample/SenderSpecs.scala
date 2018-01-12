package volkovandr.AkkaStreamsExample

import org.scalatest.FlatSpec
import volkovandr.AkkaStreamsExample.Sender.SenderIterator

class SenderSpecs extends FlatSpec {

  "A SampleSource" should "be able to generate random messages" in {
    val source = new SenderIterator(3)
    val message1 = source.next()
    val message2 = source.next()
    val message3 = source.next()
    assert(message1 != message2 || message2 != message3 || message1 != message3)
  }

  it should "stop after total amount is reached" in {
    val source = new SenderIterator(2)
    source.next()
    source.next()
    assert(!source.hasNext)
  }

  it should "work as an Iterable" in {
    val source = Sender(3)
    val result = for(message <- source) yield message
    assert(result.size == 3)
  }

}