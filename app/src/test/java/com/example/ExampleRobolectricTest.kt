package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.UserRole
import com.example.data.security.SecureSessionManager
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Cervanna Care", appName)
  }

  @Test
  fun `testSecureSessionManagerCustomerLogin`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val sessionManager = SecureSessionManager(context)

    sessionManager.saveSession(
      token = "sample_test_token_customer",
      role = UserRole.CUSTOMER,
      email = "sarah.kamau@cervannacare.ke",
      name = "Sarah Kamau"
    )

    val session = sessionManager.sessionState.value
    assertTrue(session.isLoggedIn)
    assertEquals(UserRole.CUSTOMER, session.role)
    assertEquals("Sarah Kamau", session.name)
    assertEquals("sarah.kamau@cervannacare.ke", session.email)
  }

  @Test
  fun `testSecureSessionManagerSpecialistLogin`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val sessionManager = SecureSessionManager(context)

    sessionManager.saveSession(
      token = "sample_test_token_specialist",
      role = UserRole.SPECIALIST,
      email = "faith.muthoni@cervannacare.com",
      name = "Faith Muthoni, RN",
      specialistId = "spec_1"
    )

    val session = sessionManager.sessionState.value
    assertTrue(session.isLoggedIn)
    assertEquals(UserRole.SPECIALIST, session.role)
    assertEquals("Faith Muthoni, RN", session.name)
    assertEquals("spec_1", session.specialistId)
  }

  @Test
  fun `testSecureSessionManagerClearSession`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val sessionManager = SecureSessionManager(context)

    sessionManager.saveSession(
      token = "to_be_cleared",
      role = UserRole.CUSTOMER,
      email = "test@example.com",
      name = "Test User"
    )
    assertTrue(sessionManager.sessionState.value.isLoggedIn)

    sessionManager.clearSession()
    assertFalse(sessionManager.sessionState.value.isLoggedIn)
  }
}
