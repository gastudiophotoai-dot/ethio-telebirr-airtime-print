package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.TelebirrGoldAccent
import com.example.ui.theme.TelebirrGreenPrimary
import com.example.viewmodel.AppLanguage

@Composable
fun LoginScreen(
    language: AppLanguage,
    onCustomerLogin: (phone: String, pin: String) -> Unit,
    onAdminLogin: (adminPin: String) -> Unit,
    onOpenForgetPassword: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Customer, 1 = Admin
    var phoneInput by remember { mutableStateOf("0911223344") }
    var pinInput by remember { mutableStateOf("1234") }
    var adminPinInput by remember { mutableStateOf("0000") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Banner & Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(TelebirrGreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = "Telebirr Print",
                    tint = TelebirrGoldAccent,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (language == AppLanguage.AMHARIC) "ቴሌብር ኤርታይም ፕሪንቲንግ" else "Telebirr Airtime Printing",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (language == AppLanguage.AMHARIC)
                    "የኢትዮ ቴሌ ካርድ ፕሪንት እና ደንበኞች ሂሳብ ማስተዳደሪያ"
                else
                    "Ethio Telecom Airtime Voucher & Customer Balance System",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Card Container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_card"),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Tab Selector
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            modifier = Modifier.testTag("customer_tab")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Person, contentDescription = "Customer", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.AMHARIC) "የደንበኛ መግቢያ" else "Customer Login",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            modifier = Modifier.testTag("admin_tab")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.AMHARIC) "አስተዳዳሪ (Admin)" else "Admin Access",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (selectedTab == 0) {
                        // Customer Form
                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = { phoneInput = it },
                            label = { Text(if (language == AppLanguage.AMHARIC) "የስልክ ቁጥር (Phone Number)" else "Phone Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone", tint = TelebirrGreenPrimary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_phone_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = pinInput,
                            onValueChange = { if (it.length <= 4) pinInput = it },
                            label = { Text(if (language == AppLanguage.AMHARIC) "4 አሃዝ ፒን (4-Digit PIN)" else "4-Digit PIN") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "PIN", tint = TelebirrGreenPrimary) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_pin_input")
                        )

                        // Forget Password Button (Requirement #2)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = onOpenForgetPassword,
                                modifier = Modifier.testTag("forget_password_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HelpOutline,
                                    contentDescription = "Forget PIN",
                                    modifier = Modifier.size(16.dp),
                                    tint = TelebirrGreenPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.AMHARIC) "የይለፍ ቃል ረስተዋል? (Forget PIN?)" else "Forget Password?",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TelebirrGreenPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onCustomerLogin(phoneInput, pinInput) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_customer_login_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = TelebirrGreenPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.AMHARIC) "ወደ አካውንት ግባ" else "Login to Portal",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Demo Preset Quick Fill Buttons
                        Text(
                            text = if (language == AppLanguage.AMHARIC) "የሙከራ ደንበኞች (Quick Demo Login):" else "Demo Quick Login:",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    phoneInput = "0911223344"
                                    pinInput = "1234"
                                },
                                modifier = Modifier.weight(1f).testTag("demo_login_abebe")
                            ) {
                                Text("አበበ (1234)", fontSize = 11.sp)
                            }
                            OutlinedButton(
                                onClick = {
                                    phoneInput = "0922334455"
                                    pinInput = "5678"
                                },
                                modifier = Modifier.weight(1f).testTag("demo_login_aster")
                            ) {
                                Text("አስቴር (5678)", fontSize = 11.sp)
                            }
                        }
                    } else {
                        // Admin Access Form
                        Text(
                            text = if (language == AppLanguage.AMHARIC)
                                "የአስተዳዳሪውን 4 አሃዝ ፒን (Admin PIN) ያስገቡ"
                            else
                                "Enter Agent Admin 4-Digit Security PIN",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = adminPinInput,
                            onValueChange = { if (it.length <= 4) adminPinInput = it },
                            label = { Text(if (language == AppLanguage.AMHARIC) "የአስተዳዳሪ ፒን (Admin PIN)" else "Admin PIN") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Admin PIN", tint = TelebirrGreenPrimary) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_pin_input")
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { onAdminLogin(adminPinInput) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_admin_login_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = TelebirrGreenPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.AMHARIC) "ወደ አስተዳዳሪ ፓነል ግባ" else "Enter Admin Panel",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
