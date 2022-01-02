package bot.inker.example

import bot.inker.api.plugin.JvmPlugin
import com.google.inject.Binder
import javax.inject.Singleton

@Singleton
class ExamplePlugin:JvmPlugin {
    override fun configure(binder: Binder) {

    }
}