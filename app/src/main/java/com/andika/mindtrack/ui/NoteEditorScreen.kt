package com.andika.mindtrack.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.take
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    date: String,
    viewModel: NoteViewModel,
    onBack: () -> Unit
) {
    val content by viewModel.currentNoteContent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val formattedDate = rememberFormattedDate(date)
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    
    // Local state for smoother typing
    var textState by remember { mutableStateOf("") }
    
    // Sync local state with ViewModel state (only when loading or external change)
    LaunchedEffect(content) {
        if (textState != content) {
            textState = content
        }
    }

    LaunchedEffect(date) {
        viewModel.loadNote(date)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp) // Reduced padding from 32.dp
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 26.sp // Reduced from 32.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary)
            } else {
                TextField(
                    value = textState,
                    onValueChange = { 
                        textState = it
                        viewModel.updateContent(date, it) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 500.dp),
                    placeholder = {
                        Text(
                            "Tell your story...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Light,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                            )
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 28.sp, // Adjusted line height
                        fontSize = 18.sp, // Reduced from 20.sp
                        fontWeight = FontWeight.Normal, // Changed to Normal for better readability at smaller size
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(200.dp)) // Extra space at bottom to allow scrolling past keyboard
        }
    }
}

@Composable
fun rememberFormattedDate(dateString: String): String {
    return remember(dateString) {
        try {
            val date = LocalDate.parse(dateString)
            date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault()))
        } catch (e: Exception) {
            dateString
        }
    }
}
