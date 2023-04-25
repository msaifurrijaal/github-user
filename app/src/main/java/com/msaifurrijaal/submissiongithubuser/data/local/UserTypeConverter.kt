package com.msaifurrijaal.submissiongithubuser.data.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class UserTypeConverter {

    @TypeConverter
    fun fromAnyToString(attribute: Any?): String {
        if (attribute == null) {
            return ""
        } else {
            return attribute as String
        }
    }

    @TypeConverter
    fun fromStringToAny(attribute: String?): Any {
        if (attribute == null) {
            return ""
        } else {
            return attribute
        }
    }
}