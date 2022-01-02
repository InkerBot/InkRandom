package bot.inker.random

import bot.inker.api.command.permission
import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.model.message.PlainTextComponent
import com.eloli.inkcmd.values.StringValueType
import java.math.BigInteger
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.random.nextUInt

@Singleton
@AutoComponent
class JRandom {
    @EventHandler
    fun onRegisterCommand(event:LifecycleEvent.RegisterCommand){
        event.register("random"){
            permission("random.command")
            describe = "生成随机数"
            argument("express",StringValueType.greedyString()){
                describe = "表达式，形如 1d6+2d10"
                executes {
                    val express = it.getArgument("express",String::class.java)
                    val builder = StringBuilder()
                    val parts = express.split('+')
                    if(parts.size > 255){
                        it.source.sendMessage(
                            PlainTextComponent.of("运算失败：表达式过长。")
                        )
                        return@executes
                    }
                    var sum = BigInteger.ZERO
                    for (part in parts) {
                        val nums = part.split('d', ignoreCase = true)
                        if(nums.size != 2){
                            it.source.sendMessage(
                                PlainTextComponent.of("运算失败：表达式错误：$part")
                            )
                            return@executes
                        }
                        var times:Int
                        var amount:Long
                        try {
                            times = nums[0].toUByte().toInt()
                            amount = nums[1].toUInt().toLong()
                        }catch (e:NumberFormatException){
                            it.source.sendMessage(
                                PlainTextComponent.of("运算失败：表达式错误：$part")
                            )
                            return@executes
                        }
                        var psum = BigInteger.ZERO
                        for (i in 0 until times){
                            psum += BigInteger.valueOf(Random.nextLong(1,amount+1))
                        }
                        sum += psum
                        builder.appendLine("$part: $psum")
                    }
                    builder.appendLine()
                    builder.append("结果为：$sum")
                    it.source.sendMessage(
                        PlainTextComponent.of(builder.toString())
                    )
                }
            }
        }
    }
}