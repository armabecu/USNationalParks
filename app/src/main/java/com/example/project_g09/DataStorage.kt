package com.example.project_g09

import com.example.project_g09.models.StateModel

class DataStorage {

    //Mutable list of lessons  that includes the lesson number, lesson Name, lesson length, description, video link, drawable reference and completed status
    var stateList:MutableList<StateModel> = mutableListOf(
        StateModel(	"	Alabama	"	,	"	AL	"	)	,
        StateModel(	"	Alaska	"	,	"	AK	"	)	,
        StateModel(	"	Arizona	"	,	"	AZ	"	)	,
        StateModel(	"	Arkansas	"	,	"	AR	"	)	,
        StateModel(	"	California	"	,	"	CA	"	)	,
        StateModel(	"	Colorado	"	,	"	CO	"	)	,
        StateModel(	"	Connecticut	"	,	"	CT	"	)	,
        StateModel(	"	Delaware	"	,	"	DE	"	)	,
        StateModel(	"	Florida	"	,	"	FL	"	)	,
        StateModel(	"	Georgia	"	,	"	GA	"	)	,
        StateModel(	"	Hawaii	"	,	"	HI	"	)	,
        StateModel(	"	Idaho	"	,	"	ID	"	)	,
        StateModel(	"	Illinois	"	,	"	IL	"	)	,
        StateModel(	"	Indiana	"	,	"	IN	"	)	,
        StateModel(	"	Iowa	"	,	"	IA	"	)	,
        StateModel(	"	Kansas	"	,	"	KS	"	)	,
        StateModel(	"	Kentucky	"	,	"	KY	"	)	,
        StateModel(	"	Louisiana	"	,	"	LA	"	)	,
        StateModel(	"	Maine	"	,	"	ME	"	)	,
        StateModel(	"	Maryland	"	,	"	MD	"	)	,
        StateModel(	"	Massachusetts	"	,	"	MA	"	)	,
        StateModel(	"	Michigan	"	,	"	MI	"	)	,
        StateModel(	"	Minnesota	"	,	"	MN	"	)	,
        StateModel(	"	Mississippi	"	,	"	MS	"	)	,
        StateModel(	"	Missouri	"	,	"	MO	"	)	,
        StateModel(	"	Montana	"	,	"	MT	"	)	,
        StateModel(	"	Nebraska	"	,	"	NE	"	)	,
        StateModel(	"	Nevada	"	,	"	NV	"	)	,
        StateModel(	"	New Hampshire	"	,	"	NH	"	)	,
        StateModel(	"	New Jersey	"	,	"	NJ	"	)	,
        StateModel(	"	New Mexico	"	,	"	NM	"	)	,
        StateModel(	"	New York	"	,	"	NY	"	)	,
        StateModel(	"	North Carolina	"	,	"	NC	"	)	,
        StateModel(	"	North Dakota	"	,	"	ND	"	)	,
        StateModel(	"	Ohio	"	,	"	OH	"	)	,
        StateModel(	"	Oklahoma	"	,	"	OK	"	)	,
        StateModel(	"	Oregon	"	,	"	OR	"	)	,
        StateModel(	"	Pennsylvania	"	,	"	PA	"	)	,
        StateModel(	"	Rhode Island	"	,	"	RI	"	)	,
        StateModel(	"	South Carolina	"	,	"	SC	"	)	,
        StateModel(	"	South Dakota	"	,	"	SD	"	)	,
        StateModel(	"	Tennessee	"	,	"	TN	"	)	,
        StateModel(	"	Texas	"	,	"	TX	"	)	,
        StateModel(	"	Utah	"	,	"	UT	"	)	,
        StateModel(	"	Vermont	"	,	"	VT	"	)	,
        StateModel(	"	Virginia	"	,	"	VA	"	)	,
        StateModel(	"	Washington	"	,	"	WA	"	)	,
        StateModel(	"	West Virginia	"	,	"	WV	"	)	,
        StateModel(	"	Wisconsin	"	,	"	WI	"	)	,
        StateModel(	"	Wyoming	"	,	"	WY	"	)

    )

    //Companion object constructor to make class a Singleton
    private constructor() {}
    companion object {
        @Volatile
        private lateinit var instance: DataStorage
        fun getInstance(): DataStorage {
            synchronized(this) {
                if (!Companion::instance.isInitialized) {
                    instance = DataStorage()
                }
                return instance
            }
        }
    }
}