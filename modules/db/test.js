var test = {
	"$and" : [ {
		"$and" : [ {
			"$or" : [ {
				"$and" : [ {
					"$or" : [ {
						"ad_date_from0" : {
							"$gte" : "20110214"
						}
					}, {
						"ad_date_from0" : "all"
					} ]
				}, {
					"$or" : [ {
						"ad_date_to0" : {
							"$lte" : "20110214"
						}
					}, {
						"ad_date_to0" : "all"
					} ]
				} ]
			}, {
				"$and" : [ {
					"$or" : [ {
						"ad_date_from1" : {
							"$gte" : "20110214"
						}
					}, {
						"ad_date_from1" : "all"
					} ]
				}, {
					"$or" : [ {
						"ad_date_to1" : {
							"$lte" : "20110214"
						}
					}, {
						"ad_date_to1" : "all"
					} ]
				} ]
			}, {
				"$and" : [ {
					"$or" : [ {
						"ad_date_from2" : {
							"$gte" : "20110214"
						}
					}, {
						"ad_date_from2" : "all"
					} ]
				}, {
					"$or" : [ {
						"ad_date_to2" : {
							"$lte" : "20110214"
						}
					}, {
						"ad_date_to2" : "all"
					} ]
				} ]
			}, {
				"$and" : [ {
					"$or" : [ {
						"ad_date_from3" : {
							"$gte" : "20110214"
						}
					}, {
						"ad_date_from3" : "all"
					} ]
				}, {
					"$or" : [ {
						"ad_date_to3" : {
							"$lte" : "20110214"
						}
					}, {
						"ad_date_to3" : "all"
					} ]
				} ]
			}, {
				"$and" : [ {
					"$or" : [ {
						"ad_date_from4" : {
							"$gte" : "20110214"
						}
					}, {
						"ad_date_from4" : "all"
					} ]
				}, {
					"$or" : [ {
						"ad_date_to4" : {
							"$lte" : "20110214"
						}
					}, {
						"ad_date_to4" : "all"
					} ]
				} ]
			}, {
				"$and" : [ {
					"$or" : [ {
						"ad_date_from5" : {
							"$gte" : "20110214"
						}
					}, {
						"ad_date_from5" : "all"
					} ]
				}, {
					"$or" : [ {
						"ad_date_to5" : {
							"$lte" : "20110214"
						}
					}, {
						"ad_date_to5" : "all"
					} ]
				} ]
			} ]
		} ]
	} ]
};