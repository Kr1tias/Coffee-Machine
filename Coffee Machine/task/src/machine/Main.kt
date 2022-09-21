package machine

const val ESPRESSO_WATER = 250
const val LATTE_WATER = 350
const val CAPPUCCINO_WATER = 200
const val LATTE_MILK = 75
const val CAPPUCCINO_MILK = 100
const val ESPRESSO_BEANS = 16
const val LATTE_BEANS = 20
const val CAPPUCCINO_BEANS = 12
const val ESPRESSO_COST = 4
const val LATTE_COST = 7
const val CAPPUCCINO_COST = 6

enum class State(val commands: List<String>) {
    MainMenu(listOf("buy", "fill", "take", "remaining", "exit")),
    ChoosingCoffeeType(listOf("1","2","3","back")),
    AddIngredients(emptyList())
}

class CoffeeMachine(
    private var water: Int = 400,
    private var milk: Int = 540,
    private var coffeeBeans: Int = 120,
    private var cups: Int = 9,
    private var money: Int = 550
) {
    private var state = State.MainMenu
    private var counter: Int = 0

    fun action(input: String) {
        when (state) {
            State.MainMenu -> {
                if (input !in State.MainMenu.commands) println("wrong command") else
                when (input) {
                    "buy" -> {
                        println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
                        state = State.ChoosingCoffeeType
                    }
                    "fill" -> {
                        println("Write how many ml of water do you want to add:")
                        state = State.AddIngredients
                    }
                    "take" -> {
                        money = extractCash(money)
                        println("Write action (buy, fill, take, remaining, exit):")
                    }
                    "remaining" -> {
                        currentState(water, milk, coffeeBeans, cups, money)
                        println("Write action (buy, fill, take, remaining, exit):")
                    }
                }
            }
            State.ChoosingCoffeeType -> {
                if (input !in State.ChoosingCoffeeType.commands) println("wrong command") else
                if (input == "back") {
                    state = State.MainMenu
                    println("Write action (buy, fill, take, remaining, exit):")
                } else {
                    val check = ingredientCalc(water, milk, coffeeBeans, cups, input)
                    if (check) {
                        water = useWater(water, input)
                        milk = useMilk(milk, input)
                        coffeeBeans = useBeans(coffeeBeans, input)
                        money = addMoney(money, input)
                        cups = useCups(cups)
                        state = State.MainMenu
                        println("Write action (buy, fill, take, remaining, exit):")
                    } else {
                        state = State.MainMenu
                        println("Write action (buy, fill, take, remaining, exit):")
                    }
                }
            }
            State.AddIngredients -> {
                when (counter) {
                    0 -> {
                        val addWater = input.toInt()
                        water += addWater
                        ++counter
                        println ("Write how many ml of milk do you want to add:")
                    }
                    1 -> {
                        val addMilk = input.toInt()
                        milk += addMilk
                        ++counter
                        println ("Write how many grams of coffee beans do you want to add:")
                    }
                    2 -> {
                        val addBeans = input.toInt()
                        coffeeBeans += addBeans
                        ++counter
                        println ("Write how many disposable cups of coffee do you want to add:")
                    }
                    3 -> {
                        val addCups = input.toInt()
                        cups += addCups
                        state = State.MainMenu
                        counter = 0
                        println("Write action (buy, fill, take, remaining, exit):")
                    }
                }
            }
        }
    }
}

fun main() {
    val myCoffeeMachine = CoffeeMachine()
    println("Write action (buy, fill, take, remaining, exit):")
    do {
        val input = readln()
        myCoffeeMachine.action(input)
    } while (input != "exit")
}

fun currentState(water: Int, milk: Int, coffeeBeans: Int, cups: Int, money: Int) {
    println("The coffee machine has:")
    println("$water ml of water")
    println("$milk ml of milk")
    println("$coffeeBeans g of coffee beans")
    println("$cups disposable cups")
    println("$$money of money")
}

fun useWater(water: Int, type: String): Int {
    val newWater = when (type) {
        "1" -> water - ESPRESSO_WATER
        "2" -> water - LATTE_WATER
        "3" -> water - CAPPUCCINO_WATER
        else -> water
    }
    return newWater
}

fun useMilk(milk: Int, type: String): Int {
    val newMilk = when (type) {
        "1" -> milk
        "2" -> milk - LATTE_MILK
        "3" -> milk - CAPPUCCINO_MILK
        else -> milk
    }
    return newMilk
}

fun useBeans(coffeeBeans: Int, type: String): Int {
    val newCoffeeBeans = when (type) {
        "1" -> coffeeBeans - ESPRESSO_BEANS
        "2" -> coffeeBeans - LATTE_BEANS
        "3" -> coffeeBeans - CAPPUCCINO_BEANS
        else -> coffeeBeans
    }
    return newCoffeeBeans
}

fun useCups(cups: Int): Int {
    return cups - 1
}

fun addMoney(money: Int, type: String): Int {
    val newMoney = when (type) {
        "1" -> money + ESPRESSO_COST
        "2" -> money + LATTE_COST
        "3" -> money + CAPPUCCINO_COST
        else -> money
    }
    return newMoney
}

fun ingredientCalc(water: Int, milk: Int, coffeeBeans: Int, cups: Int, type: String): Boolean {
    val remainingWater: Int
    val remainingMilk: Int
    val remainingBeans: Int
    val remainingCups: Int
    when (type) {
        "1" -> {
            remainingWater = water - ESPRESSO_WATER
            remainingMilk = milk
            remainingBeans = coffeeBeans - ESPRESSO_BEANS
            remainingCups = cups - 1
        }

        "2" -> {
            remainingWater = water - LATTE_WATER
            remainingMilk = milk - LATTE_MILK
            remainingBeans = coffeeBeans - LATTE_BEANS
            remainingCups = cups - 1
        }

        "3" -> {
            remainingWater = water - CAPPUCCINO_WATER
            remainingMilk = milk - CAPPUCCINO_MILK
            remainingBeans = coffeeBeans - CAPPUCCINO_BEANS
            remainingCups = cups - 1
        }

        else -> {
            return false
        }
    }
    return when {
        remainingWater < 0 -> {
            println("Sorry, not enough water")
            false
        }

        remainingMilk < 0 -> {
            println("Sorry, not enough milk")
            false
        }

        remainingBeans < 0 -> {
            println("Sorry, not enough coffee beans")
            false
        }

        remainingCups < 0 -> {
            println("Sorry, not enough disposable cups")
            false
        }

        else -> {
            println("I have enough resources, making you a coffee!")
            true
        }
    }
}

fun extractCash(money: Int): Int {
    println("I gave you $$money")
    return 0
}

// fun addIngredients
