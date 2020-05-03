package main

import com.espresso.data.models.history.RefuelHistoryModel
import com.espresso.data.models.refuel.RefuelProduct
import com.espresso.pbmobile.history.RefuelHistoryItemModel
import com.espresso.pbmobile.main.refueling.RefuelItemModel
import com.espresso.pbmobile.main.refueling.RefuelingFragment
import com.espresso.pbmobile.utlis.DateParser
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class MapModelsTest {

    private lateinit var fragment: RefuelingFragment

    @Before
    fun setup() {
        fragment = RefuelingFragment()
    }

    @Test
    fun test_refuelItemMaping() {
        val entryList = listOf(
            RefuelProduct(id = 1, priceBrutto = 5.0, priceNetto = 4.7, category = "Petrol", productName = "ON"),
            RefuelProduct(id = 2, priceBrutto = 4.0, priceNetto = 3.7, category = "Food", productName = "Red bull"),
            RefuelProduct(id = 3, priceBrutto = 3.0, priceNetto = 2.7, category = "Petrol", productName = "98"),
            RefuelProduct(id = 4, priceBrutto = 2.0, priceNetto = 1.7, category = "Petrol", productName = "LPG"),
            RefuelProduct(id = 5, priceBrutto = 1.0, priceNetto = 0.7, category = "Food", productName = "Snack bar")
        )

        val expectedList = listOf(
            RefuelItemModel(isClicked = false, pricePerUnit = 5.0, name = "ON", id = 1, clickHandler = {}),
            RefuelItemModel(isClicked = false, pricePerUnit = 3.0, name = "98", id = 3, clickHandler = {}),
            RefuelItemModel(isClicked = false, pricePerUnit = 2.0, name = "LPG", id = 4, clickHandler = {})
        )

        val resultList = fragment.mapRefuelItemModels(entryList)
        Assert.assertTrue(expectedList == resultList)
        Assert.assertTrue(resultList.size == 3)

        resultList.forEach {
            Assert.assertTrue(it.name == "ON" || it.name == "98" || it.name == "LPG")
        }
    }

    @Test
    fun test_historyItemMaping() {
        val entryList = listOf(
            RefuelHistoryModel(
                id = 1,
                points = 12.0,
                product = fragment.mapRefuelItemModels(
                    listOf(
                        RefuelProduct(
                            id = 1,
                            priceBrutto = 5.0,
                            priceNetto = 4.7,
                            category = "Petrol",
                            productName = "ON"
                        )
                    )
                ).get(0),
                quantity = 32.0,
                dateRefueling = "2020-04-09T17:59:39.669+0000"
            ),
            RefuelHistoryModel(
                id = 2,
                points = 15.0,
                product = fragment.mapRefuelItemModels(
                    listOf(
                        RefuelProduct(
                            id = 1,
                            priceBrutto = 5.0,
                            priceNetto = 4.7,
                            category = "Petrol",
                            productName = "ON"
                        )
                    )
                ).get(0),
                quantity = 90.0,
                dateRefueling = "2020-02-03T19:59:39.669+0000"
            )
        )

        val expectedList = listOf(
            RefuelHistoryItemModel(date = "09.04.2020 17:59", fuelType = "ON", points = 12, cost = 5.0 * 32.0),
            RefuelHistoryItemModel(date = "03.02.2020 19:59", fuelType = "ON", points = 15, cost = 5.0 * 90.0)
        )

        val resultList = fragment.mapHistoryItems(entryList)
        Assert.assertTrue(expectedList == resultList)
        Assert.assertTrue(resultList.size == 2)
        Assert.assertEquals(
            resultList[0].date,
            DateParser.parse(entryList[1].dateRefueling, DateParser.extraLongReversedPattern, DateParser.longPattern)
        )
        Assert.assertNotEquals(
            resultList[0].date,
            DateParser.parse(entryList[0].dateRefueling, DateParser.extraLongReversedPattern, DateParser.longPattern)
        )

    }

}
