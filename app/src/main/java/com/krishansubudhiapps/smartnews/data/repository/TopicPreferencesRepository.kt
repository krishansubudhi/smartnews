package com.krishansubudhiapps.smartnews.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.emptyPreferences // Import emptyPreferences

import com.krishansubudhiapps.smartnews.data.model.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import java.io.IOException
import android.util.Log // Import Log

// Define the DataStore file name
private const val TOPIC_PREFERENCES_NAME = "topic_preferences"

// Create a DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TOPIC_PREFERENCES_NAME)

class TopicPreferencesRepository(private val context: Context) {

    // Define the key for storing selected topics as a set of strings
    private val SELECTED_TOPICS_KEY = stringSetPreferencesKey("selected_topics")

    // Define the default topics
    private val defaultTopics = listOf(
        Topic(name = "Technology"),
        Topic(name = "World"),
        Topic(name = "Business")
    )

    // Flow to read the selected topics from DataStore
    val selectedTopics: Flow<List<Topic>> = context.dataStore.data
        .catch { exception ->
            // DataStore errors are serious, but we can default to an empty list or default topics
            Log.e("TopicPreferencesRepo", "Error reading preferences.", exception)
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            preferences ->
            val topics = preferences[SELECTED_TOPICS_KEY]?.map { Topic(name = it) } ?: emptyList()
            if (topics.isEmpty()) {
                // If no topics are saved, return the default topics
                defaultTopics
            } else {
                topics
            }
        }

    // Function to save the selected topics to DataStore
    suspend fun saveSelectedTopics(topics: List<Topic>) {
        context.dataStore.edit {
            preferences ->
            preferences[SELECTED_TOPICS_KEY] = topics.map { it.name }.toSet()
        }
    }
}

