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
package dev.triumphteam.core.implementations.factory

import dev.triumphteam.core.BaseCommand
import dev.triumphteam.core.command.argument.ArgumentRegistry
import dev.triumphteam.core.command.factory.AbstractCommandFactory
import dev.triumphteam.core.command.message.MessageRegistry
import dev.triumphteam.core.command.requirement.RequirementRegistry
import dev.triumphteam.core.implementations.TestCommand
import java.io.PrintStream

class TestCommandFactory(
    baseCommand: BaseCommand,
    private val argumentRegistry: ArgumentRegistry<PrintStream>,
    private val requirementRegistry: RequirementRegistry<PrintStream>,
    private val messageRegistry: MessageRegistry<PrintStream>
) : AbstractCommandFactory<TestCommand>(baseCommand) {

    override fun create(): TestCommand {
        return TestCommand(name, alias, argumentRegistry, requirementRegistry, messageRegistry)
    }
}