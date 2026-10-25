import React from 'react';
import { View, ActivityIndicator } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { Ionicons } from '@expo/vector-icons';
import { useAuth } from '../context/AuthContext';

// Customer Screens
import LoginScreen from '../screens/auth/LoginScreen';
import CustomerHomeScreen from '../screens/customer/HomeScreen';
import CustomerExploreScreen from '../screens/customer/ExploreScreen';
import CustomerBookingsScreen from '../screens/customer/BookingsScreen';
import { FavoritesScreen as CustomerFavoritesScreen } from '../screens/customer/FavoritesScreen';
import { AccountScreen as CustomerAccountScreen } from '../screens/customer/AccountScreen';
import { SpecialistDetailScreen } from '../screens/customer/SpecialistDetailScreen';

// Specialist Screens
import { SpecialistDashboardScreen } from '../screens/specialist/DashboardScreen';
import { SpecialistBookingsScreen } from '../screens/specialist/BookingsScreen';
import { SpecialistAvailabilityScreen } from '../screens/specialist/AvailabilityScreen';
import { SpecialistEarningsScreen } from '../screens/specialist/EarningsScreen';
import { SpecialistProfileScreen } from '../screens/specialist/ProfileScreen';

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();

function CustomerTabs() {
  return (
    <Tab.Navigator
      screenOptions={{
        tabBarActiveTintColor: '#0D7A68',
        tabBarInactiveTintColor: '#64748B',
        tabBarStyle: { height: 62, paddingBottom: 8, paddingTop: 6 },
        headerStyle: { backgroundColor: '#FFFFFF' },
        headerTintColor: '#0F172A',
        headerTitleStyle: { fontWeight: '700' },
      }}
    >
      <Tab.Screen
        name="CustomerHomeTab"
        component={CustomerHomeScreen}
        options={{
          title: 'Home',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="home-outline" color={color} size={size} />,
        }}
      />
      <Tab.Screen
        name="CustomerExploreTab"
        component={CustomerExploreScreen}
        options={{
          title: 'Specialists',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="search-outline" color={color} size={size} />,
        }}
      />
      <Tab.Screen
        name="CustomerBookingsTab"
        component={CustomerBookingsScreen}
        options={{
          title: 'Bookings',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="calendar-outline" color={color} size={size} />,
        }}
      />
      <Tab.Screen
        name="CustomerFavoritesTab"
        component={CustomerFavoritesScreen}
        options={{
          title: 'Saved',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="heart-outline" color={color} size={size} />,
        }}
      />
      <Tab.Screen
        name="CustomerAccountTab"
        component={CustomerAccountScreen}
        options={{
          title: 'Account',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="person-outline" color={color} size={size} />,
        }}
      />
    </Tab.Navigator>
  );
}

function SpecialistTabs() {
  return (
    <Tab.Navigator
      screenOptions={{
        tabBarActiveTintColor: '#0D7A68',
        tabBarInactiveTintColor: '#64748B',
        tabBarStyle: { height: 62, paddingBottom: 8, paddingTop: 6 },
        headerStyle: { backgroundColor: '#FFFFFF' },
        headerTintColor: '#0F172A',
        headerTitleStyle: { fontWeight: '700' },
      }}
    >
      <Tab.Screen
        name="SpecialistDashboardTab"
        component={SpecialistDashboardScreen}
        options={{
          title: 'Dashboard',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="grid-outline" color={color} size={size} />,
        }}
      />
      <Tab.Screen
        name="SpecialistBookingsTab"
        component={SpecialistBookingsScreen}
        options={{
          title: 'Shifts',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="calendar-outline" color={color} size={size} />,
        }}
      />
      <Tab.Screen
        name="SpecialistAvailabilityTab"
        component={SpecialistAvailabilityScreen}
        options={{
          title: 'Availability',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="time-outline" color={color} size={size} />,
        }}
      />
      <Tab.Screen
        name="SpecialistEarningsTab"
        component={SpecialistEarningsScreen}
        options={{
          title: 'Earnings',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="cash-outline" color={color} size={size} />,
        }}
      />
      <Tab.Screen
        name="SpecialistProfileTab"
        component={SpecialistProfileScreen}
        options={{
          title: 'Profile',
          headerShown: false,
          tabBarIcon: ({ color, size }) => <Ionicons name="person-outline" color={color} size={size} />,
        }}
      />
    </Tab.Navigator>
  );
}

export default function RootNavigator() {
  const { isAuthenticated, isLoading, userRole } = useAuth();

  if (isLoading) {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#F8FAFC' }}>
        <ActivityIndicator size="large" color="#0D7A68" />
      </View>
    );
  }

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {!isAuthenticated ? (
          <Stack.Screen name="Login" component={LoginScreen} />
        ) : userRole === 'CUSTOMER' ? (
          <>
            <Stack.Screen name="CustomerRoot" component={CustomerTabs} />
            <Stack.Screen
              name="SpecialistDetail"
              component={SpecialistDetailScreen}
              options={{ headerShown: false }}
            />
          </>
        ) : (
          <Stack.Screen name="SpecialistRoot" component={SpecialistTabs} />
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}
