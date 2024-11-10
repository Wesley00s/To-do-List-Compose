package com.example.to_dolistjetpack.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.enumeration.PriorityLevel
import com.example.to_dolistjetpack.listItem.TaskItem
import com.example.to_dolistjetpack.model.Task
import com.example.to_dolistjetpack.ui.theme.Secondary
import com.example.to_dolistjetpack.ui.theme.Tertiary
import com.example.to_dolistjetpack.ui.theme.White
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoList(
    navController: NavController,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isTextFieldFocused by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    if (isSearchVisible) {
                        isSearchVisible = false
                        searchQuery = ""
                    }
                }
            },
        containerColor = Secondary,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Tertiary
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSearchVisible) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { focusState ->
                                        isTextFieldFocused = focusState.isFocused
                                    },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = White,
                                    focusedLabelColor = Color.LightGray,
                                    unfocusedLabelColor = Color.LightGray,
                                    cursorColor = White,
                                ),
                                label = {
                                    Text(text = "Search tasks...")
                                },
                                singleLine = true,
                                maxLines = 1,
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray
                                ),
                                trailingIcon = {
                                    if (isTextFieldFocused) {
                                        IconButton(onClick = {
                                            searchQuery = ""
                                            isSearchVisible = false
                                            focusManager.clearFocus()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            )
                        } else {
                            Text(
                                text = "To-Do List",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray
                                )
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_more_vert),
                            contentDescription = "More"
                        )
                    }

                    DropdownMenu(
                        modifier = Modifier
                            .width(150.dp),
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Search",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                isSearchVisible = !isSearchVisible
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Profile",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )

                                )
                            },
                            onClick = {
                                menuExpanded = false
                                isSearchVisible = !isSearchVisible
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "About",
                                    fontSize = 16.sp,
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                isSearchVisible = !isSearchVisible
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Logout",
                                    fontSize = 16.sp,
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                isSearchVisible = !isSearchVisible
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("saveTask")
                },
                containerColor = Tertiary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val tasks: MutableList<Task> = mutableListOf(
                Task(
                    name = "Finalizar relatório do projeto",
                    description = "Completar o relatório final e enviá-lo para o cliente",
                    priority = PriorityLevel.HIGH,
                    updateAt = LocalDateTime.now().minusHours(2),
                    isDone = false
                ),
                Task(
                    name = "Reunião com a equipe de desenvolvimento",
                    description = "Discutir o progresso do sprint atual e próximos passos",
                    priority = PriorityLevel.MEDIUM,
                    updateAt = LocalDateTime.now(),
                    isDone = false
                ),
                Task(
                    name = "Revisar código do módulo de autenticação",
                    description = "Verificar o código e refatorar para melhorar a segurança",
                    priority = PriorityLevel.HIGH,
                    updateAt = LocalDateTime.now().minusDays(1),
                    isDone = false
                ),
                Task(
                    name = "Atualizar documentação do projeto",
                    description = "Adicionar novas funcionalidades e corrigir informações desatualizadas",
                    priority = PriorityLevel.LOW,
                    updateAt = LocalDateTime.now().minusDays(3),
                    isDone = true
                ),
                Task(
                    name = "Organizar arquivos e pastas no servidor",
                    description = "Realizar limpeza de arquivos antigos e reestruturar diretórios",
                    priority = PriorityLevel.NONE,
                    updateAt = LocalDateTime.now().minusDays(5),
                    isDone = false
                ),
                Task(
                    name = "Planejar nova campanha de marketing",
                    description = "Criar um plano para a próxima campanha no mês que vem",
                    priority = PriorityLevel.MEDIUM,
                    updateAt = LocalDateTime.now().minusHours(6),
                    isDone = true
                )
            )
            val filteredTasks = if (searchQuery.isNotEmpty()) {
                tasks.filter { it.name?.contains(searchQuery, ignoreCase = true) == true }
            } else {
                tasks
            }
            LazyColumn {
                itemsIndexed(filteredTasks) { index, _ ->
                    TaskItem(index, filteredTasks.toMutableList())
                }
            }
        }
    }
}