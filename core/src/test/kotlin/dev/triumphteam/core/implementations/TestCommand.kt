/**
 * MIT License
 *
 * Copyright (c) 2019-2021 Matt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.triumphteam.core.implementations

import dev.triumphteam.core.BaseCommand
import dev.triumphteam.core.annotations.Default
import dev.triumphteam.core.command.Command
import dev.triumphteam.core.command.SimpleSubCommand
import dev.triumphteam.core.command.SubCommandHolder
import dev.triumphteam.core.command.argument.ArgumentRegistry
import dev.triumphteam.core.command.message.MessageKey
import dev.triumphteam.core.command.message.MessageRegistry
import dev.triumphteam.core.command.requirement.RequirementRegistry
import dev.triumphteam.core.implementations.factory.TestSubCommandFactory
import java.io.PrintStream
import java.lang.reflect.Modifier

class TestCommand(
    private val name: String,
    private val alias: List<String>,
    private val argumentRegistry: ArgumentRegistry<PrintStream>,
    private val requirementRegistry: RequirementRegistry<PrintStream>,
    private val messageRegistry: MessageRegistry<PrintStream>
) : Command {

    private val holders: MutableMap<String, SubCommandHolder<PrintStream>> = HashMap()
    private val aliases: MutableMap<String, SubCommandHolder<PrintStream>> = HashMap()

    override fun addSubCommands(baseCommand: BaseCommand): Boolean {
        var added = false
        val methods = baseCommand.javaClass.declaredMethods

        for (method in methods) {
            if (!Modifier.isPublic(method.modifiers)) continue

            val subCommand: SimpleSubCommand<PrintStream> =
                TestSubCommandFactory(baseCommand, method, argumentRegistry, requirementRegistry).create() ?: continue

            added = true

            // TODO add this later and add aliases
            val subCommandName = subCommand.name
            val subCommandAlias = subCommand.alias

            val holder = holders[subCommandName]
            if (holder == null) {
                val newHolder = SubCommandHolder(messageRegistry, subCommand)
                holders[subCommandName] = newHolder
                for (alias in subCommandAlias) {
                    aliases[alias] = newHolder
                }
                continue
            }

            holder.add(subCommand)
            // TODO handle alias later
        }

        return added
    }

    fun execute(sender: PrintStream, args: Array<String>) {
        var subCommand = defaultSubCommand
        var subCommandName = ""

        if (args.isNotEmpty()) subCommandName = args[0].lowercase()
        if (subCommand == null || subCommandExists(subCommandName)) {
            subCommand = getSubCommand(subCommandName)
        }

        if (subCommand == null || args.isNotEmpty() && subCommand.isSmallDefault) {
            messageRegistry.sendMessage(MessageKey.WRONG_USAGE, sender)
            return
        }

        subCommand.execute(sender, listOf(*args))
        return
    }

    override fun getName(): String {
        return name
    }

    override fun getAlias(): List<String> {
        return alias
    }

    private val defaultSubCommand: SubCommandHolder<PrintStream>?
        get() = holders[Default.DEFAULT_CMD_NAME]

    private fun getSubCommand(key: String): SubCommandHolder<PrintStream>? {
        val holder = holders[key]
        return holder ?: aliases[key]
    }

    private fun subCommandExists(key: String): Boolean {
        return holders.containsKey(key) || aliases.containsKey(key)
    }
}