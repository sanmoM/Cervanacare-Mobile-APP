import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  TextInput,
  Alert,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useAuth } from '../../context/AuthContext';

export const SpecialistProfileScreen: React.FC = () => {
  const { user, logout } = useAuth();

  const [dailyRate, setDailyRate] = useState('3500');
  const [monthlyRate, setMonthlyRate] = useState('65000');
  const [biography, setBiography] = useState(
    'Registered Nurse with 6+ years experience in ICU, post-operative surgical rehabilitation, and compassionate elderly home nursing.'
  );

  const handleSaveProfile = () => {
    Alert.alert('Profile Saved', 'Your rates and biography have been updated on Cervanna Care.');
  };

  const handleLogout = () => {
    Alert.alert('Log Out', 'Are you sure you want to log out of your Specialist account?', [
      { text: 'Cancel', style: 'cancel' },
      { text: 'Log Out', style: 'destructive', onPress: () => logout() },
    ]);
  };

  return (
    <View style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scrollContent}>
        <View style={styles.header}>
          <View style={styles.avatarCircle}>
            <Text style={styles.avatarLetter}>FM</Text>
          </View>
          <Text style={styles.name}>{user?.fullName || 'Nurse Faith Muthoni'}</Text>
          <Text style={styles.category}>Registered Nurse (NCK Licensed)</Text>
          <View style={styles.verifiedTag}>
            <Ionicons name="shield-checkmark" size={14} color="#0D7A68" />
            <Text style={styles.verifiedText}>DCI Good Conduct Verified</Text>
          </View>
        </View>

        {/* Pricing Configuration */}
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Service Rates (Kenya Shillings)</Text>
          <Text style={styles.cardSubtitle}>Fair market rates for Nairobi & surrounding counties</Text>

          <Text style={styles.inputLabel}>Daily Rate (KSh / day)</Text>
          <TextInput
            style={styles.input}
            value={dailyRate}
            onChangeText={setDailyRate}
            keyboardType="numeric"
          />

          <Text style={styles.inputLabel}>Monthly Rate (KSh / month)</Text>
          <TextInput
            style={styles.input}
            value={monthlyRate}
            onChangeText={setMonthlyRate}
            keyboardType="numeric"
          />
        </View>

        {/* Biography */}
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Professional Biography</Text>
          <TextInput
            style={[styles.input, styles.textArea]}
            value={biography}
            onChangeText={setBiography}
            multiline
            numberOfLines={4}
          />
        </View>

        {/* Verification Credentials */}
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Verified Credentials</Text>
          <View style={styles.credentialRow}>
            <Ionicons name="ribbon" size={20} color="#0D7A68" />
            <View style={styles.credTextWrap}>
              <Text style={styles.credTitle}>Nursing Council of Kenya (NCK)</Text>
              <Text style={styles.credSubtitle}>License #NCK-2021-9481 • Active</Text>
            </View>
            <Ionicons name="checkmark-circle" size={20} color="#10B981" />
          </View>

          <View style={styles.credentialRow}>
            <Ionicons name="document-text" size={20} color="#0D7A68" />
            <View style={styles.credTextWrap}>
              <Text style={styles.credTitle}>DCI Certificate of Good Conduct</Text>
              <Text style={styles.credSubtitle}>Biometric clearance passed • Verified 2026</Text>
            </View>
            <Ionicons name="checkmark-circle" size={20} color="#10B981" />
          </View>
        </View>

        {/* Save Button */}
        <TouchableOpacity style={styles.saveButton} onPress={handleSaveProfile}>
          <Text style={styles.saveButtonText}>Save Profile Changes</Text>
        </TouchableOpacity>

        {/* Logout Button */}
        <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
          <Ionicons name="log-out-outline" size={20} color="#EF4444" style={{ marginRight: 8 }} />
          <Text style={styles.logoutText}>Log Out from Specialist Account</Text>
        </TouchableOpacity>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F8FAFC',
  },
  scrollContent: {
    paddingBottom: 40,
  },
  header: {
    backgroundColor: '#FFFFFF',
    alignItems: 'center',
    paddingTop: 54,
    paddingBottom: 24,
    borderBottomWidth: 1,
    borderBottomColor: '#E2E8F0',
  },
  avatarCircle: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: '#E6F4F1',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: '#0D7A68',
  },
  avatarLetter: {
    fontSize: 26,
    fontWeight: '800',
    color: '#0D7A68',
  },
  name: {
    fontSize: 20,
    fontWeight: '800',
    color: '#0F172A',
    marginTop: 10,
  },
  category: {
    fontSize: 13,
    color: '#0D7A68',
    fontWeight: '600',
    marginTop: 2,
  },
  verifiedTag: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#ECFDF5',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 6,
    marginTop: 8,
  },
  verifiedText: {
    fontSize: 11,
    color: '#065F46',
    fontWeight: '600',
    marginLeft: 4,
  },
  card: {
    backgroundColor: '#FFFFFF',
    marginHorizontal: 16,
    marginTop: 16,
    padding: 16,
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  cardTitle: {
    fontSize: 15,
    fontWeight: '700',
    color: '#0F172A',
  },
  cardSubtitle: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 2,
    marginBottom: 10,
  },
  inputLabel: {
    fontSize: 13,
    fontWeight: '600',
    color: '#334155',
    marginTop: 10,
    marginBottom: 6,
  },
  input: {
    backgroundColor: '#F8FAFC',
    borderWidth: 1,
    borderColor: '#E2E8F0',
    borderRadius: 8,
    padding: 12,
    fontSize: 14,
    color: '#0F172A',
  },
  textArea: {
    height: 90,
    textAlignVertical: 'top',
  },
  credentialRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#F1F5F9',
  },
  credTextWrap: {
    flex: 1,
    marginLeft: 12,
  },
  credTitle: {
    fontSize: 13,
    fontWeight: '600',
    color: '#0F172A',
  },
  credSubtitle: {
    fontSize: 11,
    color: '#64748B',
    marginTop: 2,
  },
  saveButton: {
    backgroundColor: '#0D7A68',
    marginHorizontal: 16,
    marginTop: 20,
    paddingVertical: 14,
    borderRadius: 12,
    alignItems: 'center',
  },
  saveButtonText: {
    color: '#FFFFFF',
    fontSize: 15,
    fontWeight: '700',
  },
  logoutButton: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FEE2E2',
    marginHorizontal: 16,
    marginTop: 14,
    paddingVertical: 14,
    borderRadius: 12,
  },
  logoutText: {
    fontSize: 14,
    fontWeight: '700',
    color: '#EF4444',
  },
});
