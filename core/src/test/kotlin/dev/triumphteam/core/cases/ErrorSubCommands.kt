package dev.triumphteam.core.cases

import dev.triumphteam.core.BaseCommand
import dev.triumphteam.core.annotations.Command
import dev.triumphteam.core.annotations.CommandFlags
import dev.triumphteam.core.annotations.Default
import dev.triumphteam.core.annotations.SubCommand
import java.io.PrintStream

@Command("foo")
class EmptyCommandMethod : BaseCommand() {

    @Default
    fun test() {}
}

@Command("foo")
class MissingSender : BaseCommand() {

    @Default
    fun test(arg: String) {}
}

@Command("foo")
class EmptySubCommand : BaseCommand() {

    @SubCommand("")
    fun test(sender: PrintStream) {}
}

@Command("foo")
class EmptyCommandFlags : BaseCommand() {

    @CommandFlags
    @SubCommand("bar")
    fun test(sender: PrintStream) {}
}
