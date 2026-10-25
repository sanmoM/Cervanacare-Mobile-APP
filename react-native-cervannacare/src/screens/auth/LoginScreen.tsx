import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  Alert,
} from 'react-native';
import { useAuth } from '../../context/AuthContext';
import { UserRole } from '../../types';

export default function LoginScreen() {
  const { login } = useAuth();
  const [isRegister, setIsRegister] = useState(false);
  const [selectedRole, setSelectedRole] = useState<UserRole>('CUSTOMER');
  const [name, setName] = useState('');
  const [emailOrPhone, setEmailOrPhone] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async () => {
    if (!emailOrPhone.trim()) {
      Alert.alert('Error', 'Please enter your email or Kenyan phone number.');
      return;
    }
    const displayName = isRegister && name.trim() ? name.trim() : (selectedRole === 'CUSTOMER' ? 'Sarah Kamau' : 'Faith Muthoni, RN');
    await login(selectedRole, emailOrPhone.trim(), displayName);
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scroll}>
        <View style={styles.brandContainer}>
          <View style={styles.logoBadge}>
            <Text style={styles.logoBadgeText}>+</Text>
          </View>
          <Text style={styles.brandTitle}>Cervanna Care</Text>
          <Text style={styles.brandSubtitle}>Kenya's Trusted Home-Care & Healthcare Marketplace</Text>
        </View>

        {/* Tab switch */}
        <View style={styles.tabContainer}>
          <TouchableOpacity
            style={[styles.tabButton, !isRegister && styles.tabActive]}
            onPress={() => setIsRegister(false)}
          >
            <Text style={[styles.tabText, !isRegister && styles.tabTextActive]}>Sign In</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.tabButton, isRegister && styles.tabActive]}
            onPress={() => setIsRegister(true)}
          >
            <Text style={[styles.tabText, isRegister && styles.tabTextActive]}>Register</Text>
          </TouchableOpacity>
        </View>

        <Text style={styles.sectionLabel}>Select Your Account Role</Text>
        <View style={styles.roleRow}>
          <TouchableOpacity
            style={[styles.roleCard, selectedRole === 'CUSTOMER' && styles.roleCardActive]}
            onPress={() => setSelectedRole('CUSTOMER')}
          >
            <Text style={styles.roleCardIcon}>👤</Text>
            <Text style={styles.roleTitle}>Customer / User</Text>
            <Text style={styles.roleDesc}>Find and book care for my family</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.roleCard, selectedRole === 'SPECIALIST' && styles.roleCardActive]}
            onPress={() => setSelectedRole('SPECIALIST')}
          >
            <Text style={styles.roleCardIcon}>🩺</Text>
            <Text style={styles.roleTitle}>Care Specialist</Text>
            <Text style={styles.roleDesc}>Nurse, caregiver or domestic staff</Text>
          </TouchableOpacity>
        </View>

        {isRegister && (
          <View style={styles.inputGroup}>
            <Text style={styles.label}>Full Name</Text>
            <TextInput
              style={styles.input}
              placeholder="e.g. Sarah Kamau"
              value={name}
              onChangeText={setName}
            />
          </View>
        )}

        <View style={styles.inputGroup}>
          <Text style={styles.label}>Email or Mobile Number</Text>
          <TextInput
            style={styles.input}
            placeholder="e.g. 0712 345 678"
            keyboardType="phone-pad"
            value={emailOrPhone}
            onChangeText={setEmailOrPhone}
          />
        </View>

        <View style={styles.inputGroup}>
          <Text style={styles.label}>Password</Text>
          <TextInput
            style={styles.input}
            placeholder="••••••••"
            secureTextEntry
            value={password}
            onChangeText={setPassword}
          />
        </View>

        <TouchableOpacity style={styles.submitButton} onPress={handleSubmit}>
          <Text style={styles.submitButtonText}>
            {isRegister
              ? `Create ${selectedRole === 'CUSTOMER' ? 'Customer' : 'Specialist'} Account`
              : `Sign In as ${selectedRole === 'CUSTOMER' ? 'Customer' : 'Specialist'}`}
          </Text>
        </TouchableOpacity>

        <View style={styles.divider} />

        <Text style={styles.quickLabel}>QUICK TEST CREDENTIALS</Text>

        <TouchableOpacity
          style={styles.demoButton}
          onPress={() => login('CUSTOMER', 'sarah.kamau@cervannacare.ke', 'Sarah Kamau')}
        >
          <Text style={styles.demoButtonText}>⚡ Sign In as Customer (Sarah Kamau)</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.demoButton, { borderColor: '#D97706' }]}
          onPress={() => login('SPECIALIST', 'faith.muthoni@cervannacare.com', 'Faith Muthoni, RN')}
        >
          <Text style={[styles.demoButtonText, { color: '#B45309' }]}>⚡ Sign In as Specialist (Faith Muthoni, RN)</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8FAFC' },
  scroll: { padding: 24, paddingBottom: 40 },
  brandContainer: { alignItems: 'center', marginTop: 10, marginBottom: 24 },
  logoBadge: {
    width: 58,
    height: 58,
    borderRadius: 16,
    backgroundColor: '#0D7A68',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 10,
  },
  logoBadgeText: { color: '#FFFFFF', fontSize: 32, fontWeight: 'bold' },
  brandTitle: { fontSize: 24, fontWeight: 'bold', color: '#09584B' },
  brandSubtitle: { fontSize: 12, color: '#64748B', textAlign: 'center', marginTop: 4 },
  tabContainer: { flexDirection: 'row', backgroundColor: '#E2E8F0', borderRadius: 12, padding: 4, marginBottom: 20 },
  tabButton: { flex: 1, paddingVertical: 10, alignItems: 'center', borderRadius: 10 },
  tabActive: { backgroundColor: '#FFFFFF' },
  tabText: { fontSize: 13, color: '#64748B' },
  tabTextActive: { fontWeight: 'bold', color: '#0D7A68' },
  sectionLabel: { fontSize: 13, fontWeight: '600', color: '#0F172A', marginBottom: 8 },
  roleRow: { flexDirection: 'row', gap: 10, marginBottom: 16 },
  roleCard: {
    flex: 1,
    padding: 12,
    borderRadius: 14,
    borderWidth: 1.5,
    borderColor: '#E2E8F0',
    backgroundColor: '#FFFFFF',
  },
  roleCardActive: { borderColor: '#0D7A68', backgroundColor: '#F0FDFA' },
  roleCardIcon: { fontSize: 22, marginBottom: 4 },
  roleTitle: { fontSize: 12, fontWeight: 'bold', color: '#0F172A' },
  roleDesc: { fontSize: 10, color: '#64748B', marginTop: 2 },
  inputGroup: { marginBottom: 14 },
  label: { fontSize: 12, fontWeight: '500', color: '#334155', marginBottom: 4 },
  input: {
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#CBD5E1',
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 10,
    fontSize: 14,
  },
  submitButton: {
    backgroundColor: '#0D7A68',
    borderRadius: 12,
    paddingVertical: 14,
    alignItems: 'center',
    marginTop: 8,
  },
  submitButtonText: { color: '#FFFFFF', fontSize: 15, fontWeight: 'bold' },
  divider: { height: 1, backgroundColor: '#E2E8F0', marginVertical: 20 },
  quickLabel: { fontSize: 11, fontWeight: 'bold', color: '#64748B', letterSpacing: 0.8, marginBottom: 10 },
  demoButton: {
    borderWidth: 1,
    borderColor: '#0D7A68',
    borderRadius: 12,
    paddingVertical: 12,
    alignItems: 'center',
    marginBottom: 10,
    backgroundColor: '#FFFFFF',
  },
  demoButtonText: { color: '#0D7A68', fontSize: 13, fontWeight: '600' },
});
