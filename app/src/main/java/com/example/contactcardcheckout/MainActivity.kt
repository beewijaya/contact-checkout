package com.example.contactcardcheckout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactcardcheckout.ui.theme.ContactCardCheckoutTheme
import com.example.contactcardcheckout.ui.theme.PrimaryGrey
import com.example.contactcardcheckout.ui.theme.TertiaryGrey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactCardCheckoutTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("contact") }

    when (currentScreen) {
        "contact" -> ContactCardScreen(
            onNavigateToCheckout = { currentScreen = "checkout" }
        )
        "checkout" -> CheckoutScreen(
            onNavigateToContact = { currentScreen = "contact" }
        )
    }
}

@Composable
fun AlertDialogPrev(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Ok")
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactCardScreen(onNavigateToCheckout: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val dialogState = remember { mutableStateOf(false) }


    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    when {
        dialogState.value -> {
            AlertDialogPrev(
                onConfirmation = {
                    dialogState.value = false
                    onNavigateToCheckout()
                 },
                dialogTitle = "Ringkasan",
                dialogText = "Nama: ${name}\nEmail: ${email}",
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Contact Preview"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGrey,
                    titleContentColor = TertiaryGrey,
                ),
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier.padding(innerPadding).padding(30.dp),
            horizontalArrangement = Arrangement.Center,

        ) {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = it.isBlank()
                    },
                    label = { Text("Nama") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError
                )
                if (nameError) {
                    Text(
                        text = "Nama harus diisi",
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !it.contains("@") || it.isBlank()
                    },
                    label = {Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError
                )
                if (emailError) {
                    Text(
                        text = "Alamat email tidak valid",
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {
                        if (!emailError && !nameError) {
                            dialogState.value = true
                        }
                    },
                    enabled = (!name.isBlank() && !email.isBlank()),
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text("Preview")
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun ContactCardScreenPreview() {
    ContactCardCheckoutTheme {
        ContactCardScreen(fun() {})
    }
}


// -------------------------------- //

@Composable
fun AlertDialogCheckout(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    name: String,
    phone: String,
    address: String,
    method: String,
    notes: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text("Nama: $name")
                Text("Telepon: $phone")
                Text("Alamat: $address")
                Text("Pengiriman: $method")
                Text("Catatan: ${if (notes.isBlank()) "(kosong)" else notes}")
            }
        },
        onDismissRequest = { },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text("OK")
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(onNavigateToContact: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("Regular") }
    var notes by remember { mutableStateOf("") }
    var isAgreeChecked by remember { mutableStateOf(false) }
    val dialogState = remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }

    val isFormValid = name.isNotBlank() &&
            phone.isNotBlank() &&
            phone.length in 10..15 &&
            phone.all { it.isDigit() } &&
            address.isNotBlank() &&
            isAgreeChecked

    when {
        dialogState.value -> {
            AlertDialogCheckout(
                onConfirmation = {
                    dialogState.value = false
                    onNavigateToContact()
                 },
                dialogTitle = "Ringkasan Pesanan",
                name = name,
                phone = phone,
                address = address,
                method = selectedMethod,
                notes = notes
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Checkout")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGrey,
                    titleContentColor = TertiaryGrey,
                ),
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .padding(30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
        ) {
            Column {
                Text(
                    text = "Nama Penerima",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = it.isBlank()
                    },
                    placeholder = { Text("Nama lengkap") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError
                )
                if (nameError && name.isNotEmpty()) {
                    Text(
                        text = "Nama harus diisi",
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Telepon",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            phone = it
                            phoneError = it.length !in 10..15
                        }
                    },
                    placeholder = { Text("08xxxxxxxxxx") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = phoneError
                )
                if (phoneError && phone.isNotEmpty()) {
                    Text(
                        text = "Hanya digit, 10-15",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Alamat",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        addressError = it.isBlank()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4,
                    isError = addressError
                )
                if (addressError && address.isNotEmpty()) {
                    Text(
                        text = "Alamat harus diisi",
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Metode Pengiriman",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Regular", "Express", "Same Day").forEach { metode ->
                        MetodeChipSimple(
                            label = metode,
                            isSelected = selectedMethod == metode,
                            onClick = { selectedMethod = metode }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Catatan (opsional)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isAgreeChecked = !isAgreeChecked },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAgreeChecked,
                        onCheckedChange = { isAgreeChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryGrey
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Saya setuju dengan Syarat & Ketentuan",
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        nameError = name.isBlank()
                        phoneError = phone.length !in 10..15
                        addressError = address.isBlank()

                        if (isFormValid) {
                            dialogState.value = true
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text("Buat Pesanan", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MetodeChipSimple(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) PrimaryGrey else Color(0xFFE8E8E8),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) TertiaryGrey else Color.DarkGray,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    ContactCardCheckoutTheme {
        CheckoutScreen(fun() {})
    }
}
