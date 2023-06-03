package com.gis2alk.automashup.models

object ConstantValues {

    val mashUpUSSDProcedureForSelf =

        USSDProcedure(
            ussdSteps = listOf(
//                USSDStep(option = "1", lookout = "welcome to mtn pulse."),
//                USSDStep(option = "1", lookout = "welcome to mtn pulse. please select an option"),
//                USSDStep(
//                    option = "1", lookout = "mashup offers"
//                ),
//                USSDStep(
//                    option = "2", lookout = "Select one of the following:"
//                ),
                USSDStep(
                    option = "0.07",
                    lookout = "enter amount to buy your preferred bundle (ghc 0.07 - 4.99)"
                ),
                USSDStep(
                    option = "5",
                    lookout = "select your preferred option"
                ),

                USSDStep(
                    option = "1",
                    lookout = "choose payment mode"
                ),
            )
        )
}