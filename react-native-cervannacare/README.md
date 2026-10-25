# Cervanna Care Mobile App — React Native (Android & iOS)

A complete, production-ready React Native / Expo application built with TypeScript for **Cervanna Care** (https://cervannacare.com/), Kenya's premier healthcare and home-care marketplace connecting families with vetted nurses, caregivers, physiotherapists, and domestic specialists.

---

## 1. Key Architectural Features

### Strictly Two User Roles (Zero Admin in Mobile App)
- **Role 1: Customer / User**: Browse vetted care professionals across 7 core services, book shifts with price transparency, pay securely via Safaricom Lipa Na M-Pesa STK push, manage favorites and booking history.
- **Role 2: Specialist**: Toggle live availability across Kenya counties/towns, review and accept/decline incoming shift requests, mark completed visits, track 15% healthcare commission vs. flat KSh 500/mo domestic subscription, and initiate instant M-Pesa B2C withdrawals.
- **No Admin Interface**: In strict compliance with security standards, all administrative oversight and approval dashboards remain exclusively on the web administration backend.

### Kenyan Marketplace Tailoring
- **Currency**: All rates and ledgers configured in Kenya Shillings (**KSh**).
- **Payment Gateway**: Simulated Safaricom Lipa Na M-Pesa STK push (Paybill 890200) with instant confirmation and transaction reference numbers (`QKT...KE`).
- **Fair Pricing Model**:
  - **15% Platform Commission** applied to healthcare visits (Registered Nurses, Physiotherapists, Care Assistants).
  - **KSh 500 / month flat subscription** for domestic workers (House Managers, Nannies) with 0% commission on household salary.
- **Emergency Hotline**: 24/7 dedicated Nairobi care helpline (`+254 700 225533`).

---

## 2. Project Structure

```
react-native-cervannacare/
├── package.json                 # Expo SDK 52, React Navigation, Expo SecureStore
├── app.json                     # App configuration (name, bundleId, scheme)
├── tsconfig.json                # TypeScript compiler configuration
├── App.tsx                      # Root component with SafeAreaProvider & AuthProvider
├── src/
│   ├── types/
│   │   └── index.ts             # Strong types for Specialists, Bookings, Subscriptions, Roles
│   ├── context/
│   │   └── AuthContext.tsx      # Secure session storage with expo-secure-store & role routing
│   ├── navigation/
│   │   └── RootNavigator.tsx    # Native Stack & M3 Bottom Tabs for Customer vs Specialist
│   ├── services/
│   │   └── mockData.ts          # Kenyan seed data for specialists, bookings, commission, and plans
│   ├── components/
│   │   └── MpesaModal.tsx       # Interactive Lipa Na M-Pesa STK push dialog
│   └── screens/
│       ├── auth/
│       │   └── LoginScreen.tsx  # Dual-role authentication & 1-tap quick demo login
│       ├── customer/
│       │   ├── HomeScreen.tsx   # Categories, featured specialists, Kenyan location selector
│       │   ├── ExploreScreen.tsx# Search & multi-parameter filtering
│       │   ├── SpecialistDetailScreen.tsx # Credentials, bio, pricing breakdown & booking
│       │   ├── BookingsScreen.tsx # Shift schedule, M-Pesa pay status & filters
│       │   ├── FavoritesScreen.tsx# Shortlisted caregivers
│       │   └── AccountScreen.tsx# Profile, notification toggles, hotline, delete account
│       └── specialist/
│           ├── DashboardScreen.tsx   # Live toggle, key metrics, incoming shift requests
│           ├── BookingsScreen.tsx    # Scheduled patient visits, call client, complete shift
│           ├── AvailabilityScreen.tsx# Day picker, shift arrangements, Kenyan towns roster
│           ├── EarningsScreen.tsx    # 15% platform ledger, KSh 500 plan, M-Pesa B2C payout
│           └── ProfileScreen.tsx     # Rates, bio, NCK license & DCI clearance status
```

---

## 3. How to Run Locally

### Prerequisites
- Node.js (version 18 or newer)
- npm or yarn
- Expo Go on an iOS or Android device, or Android Studio / Xcode emulators

### Installation
```bash
cd react-native-cervannacare
npm install
```

### Start Development Server
```bash
npx expo start
```
- Press `a` to run on an attached Android device / emulator.
- Press `i` to run on an iOS simulator.
- Scan the displayed QR code with the **Expo Go** mobile app on Android or iOS.

### Testing User Roles in 1 Tap
On the Login screen:
1. Tap **"Try Demo as Customer"** to experience the patient/family booking and M-Pesa payment flow.
2. Or tap **"Try Demo as Specialist"** to experience the nurse/caregiver shift management, availability, and earnings withdrawal flow.
