package com.example.firsthomework

import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import java.nio.file.Paths

enum class ImageStyle {
    CIRCLE, RECTANGLE
}

class Config {
    var alpha = 0.9
    var openTime = 10000.0
    var imageType = ImageStyle.CIRCLE
    var title = "ТЫЙЪ"
    var message = "ЫФФЙ"
    var appName = "Ырфь"
    var appPositionX = "Right" // Left, Right
    var appPositionY = "Bottom" // Top, Bottom
    var openSonudPath = "D:/POPhomework/FirstHomework/src/main/resources/beat.mp3"
    var openSoundDuration = 2500.0 // miliseconds
    var animationType = "Translate" // Fade, Translate
    var image = "https://avatars.mds.yandex.net/i?id=ee0a8cd0c69a411b7fee131fde2b4980-3732926-images-thumbs&n=13"
    var button1On = true
    var button2On = true
    var leftPadding = 15.0
    var rightPadding = 15.0
    var topPadding = 15.0
    var bottomPadding = 15.0
    var messageStyle = "-fx-text-fill: green; -fx-font-size: 20px;"
    var titleStyle = "-fx-text-fill: red; -fx-font-size: 20px;"
    var appNameStyle = "-fx-text-fill: blue; -fx-font-size: 16px;"
}

class Toast {
    private var config = Config()
    private val windows = Stage()
    private var root = BorderPane()
    private var box = HBox()
    private val primaryScreenBounds = Screen.getPrimary().visualBounds
    private var iconBorder = if (config.imageType == ImageStyle.RECTANGLE) {
        Rectangle(100.0, 100.0)
    }
    else {
        Circle(50.0, 50.0, 50.0)
    }


    class Builder {
        private var config = Config()

        fun setTitle(str: String): Builder {
            config.title = str
            return this
        }

        fun setMessage(str: String): Builder {
            config.message = str;
            return this
        }

        fun setAppName(str: String): Builder {
            config.appName = str
            return this
        }

        fun build(): Toast  {
            var toast = Toast()
            toast.config = config
            toast.build()

            return toast
        }

    }


    private fun build() {
        windows.initStyle(StageStyle.TRANSPARENT)


        setImage()
        openSound()

        val vbox = VBox()

        var title = Label(config.title)
        var message = Label(config.message)
        var appName = Label(config.appName)


        title.style = config.titleStyle
        message.style = config.messageStyle
        appName.style = config.appNameStyle
        vbox.children.addAll(title, message, appName)

        if (config.button1On) {
            var button1 = Button("JoJoButton")
            vbox.children.add(button1)

            button1.addEventHandler(
                MouseEvent.MOUSE_CLICKED
            ) { title.text = "Yare"
                message.text = "Yare"
                appName.text = "Daze"
                iconBorder.fill = ImagePattern(Image("D:/POPhomework/FirstHomework/src/main/resources/original.png"))
                windows.sizeToScene()}
        }
        if (config.button2On) {
            var button2 = Button("RandomButton")
            vbox.children.add(button2)

            button2.addEventHandler(
                MouseEvent.MOUSE_CLICKED
            ) { windows.x = (0..(primaryScreenBounds.width - windows.scene.width).toInt()).random().toDouble()
                windows.y = (0..(primaryScreenBounds.height - windows.scene.height).toInt()).random().toDouble()}
        }


        box.children.add(vbox)
        root.center = box

        windows.scene = Scene(root)
        windows.scene.fill = Color.TRANSPARENT
        windows.sizeToScene()

        root.style = "-fx-background-color: #ffffff"
        root.padding = Insets(config.topPadding, config.rightPadding, config.bottomPadding, config.leftPadding)

    }

    private fun setImage() {
        if (config.image.isEmpty()) {
            return
        }

        iconBorder.fill = ImagePattern(Image(config.image))
        box.children.add(iconBorder)
    }

    private fun openSound() {
        if (config.openSonudPath.isEmpty()) {
            return
        }

        var h = Media(Paths.get(config.openSonudPath).toUri().toString())
        var mediaPlayer = MediaPlayer(h);
        mediaPlayer.stopTime = Duration(config.openSoundDuration);
        mediaPlayer.play();

    }

    private fun openAnimation() {

        if (config.appPositionX == "Left"){
            windows.x = 0.0
        }else if (config.appPositionX == "Right"){
            windows.x = primaryScreenBounds.width - windows.scene.width
        }

        if (config.appPositionY == "Top"){
            windows.y = 0.0
        }else if (config.appPositionY == "Bottom"){
            windows.y = primaryScreenBounds.height - windows.scene.height
        }

        if (config.animationType == "Fade"){
            val anim = FadeTransition(Duration.millis(1500.0), root)
            anim.fromValue = 0.0
            anim.toValue = config.alpha
            anim.cycleCount = 1
            anim.play()
        }else if (config.animationType == "Translate"){
            val anim = TranslateTransition(Duration.millis(1500.0), root)

            if (config.appPositionX == "Left"){
                anim.fromX = -windows.scene.width;
                anim.byX = windows.scene.width;
            }else if (config.appPositionX == "Right"){
                anim.fromX = windows.scene.width;
                anim.byX = -windows.scene.width;
            }
            anim.cycleCount = 1
            anim.play()
        }
    }
    private fun closeAnimation() {
        val anim = TranslateTransition(Duration.millis(1500.0), root)


        if (config.animationType == "Fade") {
            val anim = FadeTransition(Duration.millis(1500.0), root)
            anim.fromValue = config.alpha
            anim.toValue = 0.0
            anim.cycleCount = 1
            anim.onFinished = EventHandler {
                Platform.exit()
                System.exit(0)
            }
            anim.play()
        }else if (config.animationType == "Translate"){
            if (config.appPositionY == "Top") {
                anim.byY = -windows.scene.height;
            } else if (config.appPositionY == "Bottom") {
                anim.byY = primaryScreenBounds.height + windows.scene.height
            }
            anim.cycleCount = 1
            anim.onFinished = EventHandler {
                Platform.exit()
                System.exit(0)
            }
            anim.play()
        }
    }

    fun start() {
        windows.show()
        openAnimation();
        val thread = Thread {
            try {
                Thread.sleep(config.openTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            closeAnimation()
        }
        Thread(thread).start()
    }

}


class SomeClass: Application() {
    override fun start(p0: Stage?) {
        var toast = Toast.Builder()
            .build()
        toast.start()
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SomeClass::class.java)
        }
    }
}