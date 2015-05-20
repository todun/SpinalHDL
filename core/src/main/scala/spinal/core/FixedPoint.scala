package spinal.core

import scala.collection.mutable.ArrayBuffer

/**
 * Created by PIC on 19.05.2015.
 */


object SFix{
  def apply(exp : Int,bitCount : Int) = new SFix(exp,bitCount)
}

@valClone
class SFix(val exp : Int,val bitCount : Int) extends Bundle{
  //override type SSelf = SFix

  val raw = SInt(bitCount bit)

  def difLsb(that : SFix) = (this.exp-this.bitCount) - (that.exp-that.bitCount)
  def alignLsb(that : SFix) : Tuple2[SInt,SInt] = {
    val lsbDif = difLsb(that)
    val left = if(lsbDif > 0) (this.raw << lsbDif) else this.raw
    val right = if(lsbDif < 0) (that.raw << -lsbDif) else that.raw
    (left,right)
  }

  def +(that : SFix): SFix = {
    val (rawLeft,rawRight) = alignLsb(that)
    val ret = new SFix(Math.max(this.exp,that.exp),Math.max(rawLeft.getWidth,rawRight.getWidth))
    ret.raw := rawLeft + rawRight
    ret
  }

  def *(that : SFix): SFix = {
    val ret = new SFix(this.exp + that.exp,Math.max(this.bitCount,that.bitCount))
    ret.raw := this.raw *that.raw
    ret
  }

  def <(that : SFix): Bool = {
    val (rawLeft,rawRight) = alignLsb(that)
    rawLeft < rawRight
  }

//  override def :=(that: SSelf): Unit = {
//    val difLsb = this.difLsb(that)
//    if(difLsb > 0)
//      this.raw := that.raw >> difLsb
//    else if(difLsb < 0)
//      that.raw << -difLsb
//    else
//      this.raw := that.raw
//  }
}