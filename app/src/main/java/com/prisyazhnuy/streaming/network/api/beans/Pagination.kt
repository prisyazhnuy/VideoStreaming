package com.prisyazhnuy.streaming.network.api.beans

import com.fasterxml.jackson.annotation.JsonProperty


data class Pagination(@JsonProperty("total")
                      val total: Int?,
                      @JsonProperty("nextOffset")
                      val nextOffset: Int?,
                      @JsonProperty("nextPage")
                      val nextPage: Int?)
