import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Switch,
  Alert,
  Linking,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useAuth } from '../../context/AuthContext';

export const AccountScreen: React.FC = () => {
  const { user, logout } = useAuth();

  const [pushEnabled, setPushEnabled] = useState(true);
  const [smsEnabled, setSmsEnabled] = useState(true);
  const [emailEnabled, setEmailEnabled] = useState(false);

  const handleCallSupport = () => {
    Linking.openURL('tel:+254700225533');
  };

  const handleOpenHelpWebsite = () => {
    Linking.openURL('https://cervannacare.com/');
  };

  const handleDeleteAccount = () => {
    Alert.alert(
      'Request Account Deletion',
      'In accordance with data protection and privacy policies, your account deletion request will be processed within 48 hours. All personal details will be permanently scrubbed from Cervanna Care.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Confirm Deletion',
          style: 'destructive',
          onPress: async () => {
            Alert.alert('Request Submitted', 'Your account has been scheduled for deletion.');
            await logout();
          },
        },
      ]
    );
  };

  const handleLogout = () => {
    Alert.alert('Log Out', 'Are you sure you want to log out of Cervanna Care?', [
      { text: 'Cancel', style: 'cancel' },
      { text: 'Log Out', style: 'destructive', onPress: () => logout() },
    ]);
  };

  return (
    <View style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scrollContent}>
        {/* Header Profile Section */}
        <View style={styles.header}>
          <View style={styles.avatarCircle}>
            <Text style={styles.avatarInitials}>
              {user?.fullName
                ? user.fullName
                    .split(' ')
                    .map((n) => n[0])
                    .join('')
                    .toUpperCase()
                : 'CC'}
            </Text>
          </View>
          <Text style={styles.userName}>{user?.fullName || 'Wanjiku Kamau'}</Text>
          <Text style={styles.userRole}>Verified Client • Kenya</Text>
          <Text style={styles.userContact}>{user?.email || 'wanjiku.kamau@cervannacare.ke'}</Text>
          <Text style={styles.userPhone}>{user?.phone || '+254 712 345 678'}</Text>
        </View>

        {/* Location & Coverage */}
        <View style={styles.card}>
          <Text style={styles.sectionHeader}>Service Location</Text>
          <View style={styles.row}>
            <Ionicons name="location" size={20} color="#0D7A68" />
            <View style={styles.rowTextWrap}>
              <Text style={styles.rowTitle}>Primary Address</Text>
              <Text style={styles.rowSubtitle}>Eastern Bypass, Ruiru, Kiambu / Nairobi</Text>
            </View>
            <TouchableOpacity>
              <Text style={styles.editLink}>Edit</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Notification Preferences */}
        <View style={styles.card}>
          <Text style={styles.sectionHeader}>Notifications</Text>
          <View style={styles.toggleRow}>
            <View style={styles.toggleTextWrap}>
              <Text style={styles.toggleTitle}>Push Notifications</Text>
              <Text style={styles.toggleSubtitle}>Instant booking updates and arrival alerts</Text>
            </View>
            <Switch
              value={pushEnabled}
              onValueChange={setPushEnabled}
              trackColor={{ false: '#CBD5E1', true: '#94E0D1' }}
              thumbColor={pushEnabled ? '#0D7A68' : '#F1F5F9'}
            />
          </View>
          <View style={styles.toggleRow}>
            <View style={styles.toggleTextWrap}>
              <Text style={styles.toggleTitle}>SMS & M-Pesa Alerts</Text>
              <Text style={styles.toggleSubtitle}>Receive SMS receipts and OTP codes</Text>
            </View>
            <Switch
              value={smsEnabled}
              onValueChange={setSmsEnabled}
              trackColor={{ false: '#CBD5E1', true: '#94E0D1' }}
              thumbColor={smsEnabled ? '#0D7A68' : '#F1F5F9'}
            />
          </View>
          <View style={styles.toggleRow}>
            <View style={styles.toggleTextWrap}>
              <Text style={styles.toggleTitle}>Email Summaries</Text>
              <Text style={styles.toggleSubtitle}>Monthly invoices and health tip newsletters</Text>
            </View>
            <Switch
              value={emailEnabled}
              onValueChange={setEmailEnabled}
              trackColor={{ false: '#CBD5E1', true: '#94E0D1' }}
              thumbColor={emailEnabled ? '#0D7A68' : '#F1F5F9'}
            />
          </View>
        </View>

        {/* Support & Hotline */}
        <View style={styles.card}>
          <Text style={styles.sectionHeader}>Help & Emergency Support</Text>
          <TouchableOpacity style={styles.menuItem} onPress={handleCallSupport}>
            <Ionicons name="call" size={20} color="#0D7A68" />
            <View style={styles.menuItemTextWrap}>
              <Text style={styles.menuItemTitle}>24/7 Kenya Care Hotline</Text>
              <Text style={styles.menuItemSubtitle}>+254 700 225533 (Nairobi)</Text>
            </View>
            <Ionicons name="chevron-forward" size={18} color="#94A3B8" />
          </TouchableOpacity>

          <TouchableOpacity style={styles.menuItem} onPress={handleOpenHelpWebsite}>
            <Ionicons name="globe-outline" size={20} color="#0D7A68" />
            <View style={styles.menuItemTextWrap}>
              <Text style={styles.menuItemTitle}>Visit cervannacare.com</Text>
              <Text style={styles.menuItemSubtitle}>Terms, Care Policy & FAQ</Text>
            </View>
            <Ionicons name="chevron-forward" size={18} color="#94A3B8" />
          </TouchableOpacity>
        </View>

        {/* Security & Account Management */}
        <View style={styles.card}>
          <Text style={styles.sectionHeader}>Account & Privacy</Text>
          <TouchableOpacity
            style={styles.menuItem}
            onPress={() => Alert.alert('Change Password', 'Password reset instructions sent to your email.')}
          >
            <Ionicons name="lock-closed-outline" size={20} color="#64748B" />
            <View style={styles.menuItemTextWrap}>
              <Text style={styles.menuItemTitle}>Change Password</Text>
              <Text style={styles.menuItemSubtitle}>Update your security credentials</Text>
            </View>
            <Ionicons name="chevron-forward" size={18} color="#94A3B8" />
          </TouchableOpacity>

          <TouchableOpacity style={styles.menuItem} onPress={handleDeleteAccount}>
            <Ionicons name="trash-outline" size={20} color="#EF4444" />
            <View style={styles.menuItemTextWrap}>
              <Text style={[styles.menuItemTitle, { color: '#EF4444' }]}>Delete Account</Text>
              <Text style={styles.menuItemSubtitle}>Permanently remove data and bookings</Text>
            </View>
            <Ionicons name="chevron-forward" size={18} color="#94A3B8" />
          </TouchableOpacity>
        </View>

        {/* Log Out */}
        <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
          <Ionicons name="log-out-outline" size={20} color="#EF4444" style={{ marginRight: 8 }} />
          <Text style={styles.logoutText}>Log Out</Text>
        </TouchableOpacity>

        <Text style={styles.footerVersion}>Cervanna Care Mobile • Version 1.0.0 (Kenya)</Text>
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
  avatarInitials: {
    fontSize: 28,
    fontWeight: '800',
    color: '#0D7A68',
  },
  userName: {
    fontSize: 20,
    fontWeight: '700',
    color: '#0F172A',
    marginTop: 12,
  },
  userRole: {
    fontSize: 13,
    color: '#0D7A68',
    fontWeight: '600',
    marginTop: 2,
  },
  userContact: {
    fontSize: 13,
    color: '#64748B',
    marginTop: 4,
  },
  userPhone: {
    fontSize: 13,
    color: '#64748B',
    marginTop: 2,
  },
  card: {
    backgroundColor: '#FFFFFF',
    marginTop: 16,
    paddingHorizontal: 16,
    paddingVertical: 14,
    borderTopWidth: 1,
    borderBottomWidth: 1,
    borderColor: '#E2E8F0',
  },
  sectionHeader: {
    fontSize: 14,
    fontWeight: '700',
    color: '#0F172A',
    marginBottom: 12,
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  rowTextWrap: {
    flex: 1,
    marginLeft: 12,
  },
  rowTitle: {
    fontSize: 14,
    fontWeight: '600',
    color: '#0F172A',
  },
  rowSubtitle: {
    fontSize: 13,
    color: '#64748B',
    marginTop: 2,
  },
  editLink: {
    fontSize: 13,
    fontWeight: '600',
    color: '#0D7A68',
  },
  toggleRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#F1F5F9',
  },
  toggleTextWrap: {
    flex: 1,
    marginRight: 12,
  },
  toggleTitle: {
    fontSize: 14,
    fontWeight: '600',
    color: '#0F172A',
  },
  toggleSubtitle: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 2,
  },
  menuItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#F1F5F9',
  },
  menuItemTextWrap: {
    flex: 1,
    marginLeft: 12,
  },
  menuItemTitle: {
    fontSize: 14,
    fontWeight: '600',
    color: '#0F172A',
  },
  menuItemSubtitle: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 2,
  },
  logoutButton: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FEE2E2',
    marginHorizontal: 16,
    marginTop: 24,
    paddingVertical: 14,
    borderRadius: 12,
  },
  logoutText: {
    fontSize: 15,
    fontWeight: '700',
    color: '#EF4444',
  },
  footerVersion: {
    textAlign: 'center',
    fontSize: 12,
    color: '#94A3B8',
    marginTop: 20,
  },
});
