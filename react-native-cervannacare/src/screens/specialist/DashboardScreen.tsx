import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Switch,
  Alert,
  Image,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useAuth } from '../../context/AuthContext';
import { INITIAL_BOOKINGS, INITIAL_SPECIALISTS } from '../../services/mockData';
import { Booking } from '../../types';

export const SpecialistDashboardScreen: React.FC = () => {
  const { user } = useAuth();
  const [isOnline, setIsOnline] = useState(true);
  const [bookings, setBookings] = useState<Booking[]>(INITIAL_BOOKINGS);

  const pendingBookings = bookings.filter((b) => b.status === 'PENDING');
  const upcomingBookings = bookings.filter((b) => b.status === 'CONFIRMED' || b.status === 'IN_PROGRESS');

  const handleAccept = (bookingId: string) => {
    setBookings((prev) =>
      prev.map((b) => (b.id === bookingId ? { ...b, status: 'CONFIRMED' } : b))
    );
    Alert.alert('Booking Accepted', 'The client has been notified. You can view booking details in your shifts tab.');
  };

  const handleDecline = (bookingId: string) => {
    setBookings((prev) =>
      prev.map((b) => (b.id === bookingId ? { ...b, status: 'CANCELLED' } : b))
    );
    Alert.alert('Booking Declined', 'The booking request has been declined.');
  };

  return (
    <View style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scrollContent}>
        {/* Top Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.greeting}>Welcome back,</Text>
            <Text style={styles.specialistName}>{user?.fullName || 'Nurse Faith Muthoni'}</Text>
            <View style={styles.badgeRow}>
              <Ionicons name="shield-checkmark" size={14} color="#400022" />
              <Text style={styles.badgeText}>NCK Licensed & DCI Good Conduct Verified</Text>
            </View>
          </View>
          <View style={styles.avatarPlaceholder}>
            <Text style={styles.avatarLetter}>FM</Text>
          </View>
        </View>

        {/* Live Availability Toggle Card */}
        <View style={styles.availabilityCard}>
          <View style={styles.availInfo}>
            <View style={styles.statusRow}>
              <View style={[styles.statusDot, { backgroundColor: isOnline ? '#10B981' : '#94A3B8' }]} />
              <Text style={styles.availTitle}>
                {isOnline ? 'Available for New Care Requests' : 'Offline / On Leave'}
              </Text>
            </View>
            <Text style={styles.availSubtitle}>
              {isOnline
                ? 'Your profile is visible to families searching in Nairobi & Kiambu.'
                : 'Turn toggle on when ready to receive care bookings.'}
            </Text>
          </View>
          <Switch
            value={isOnline}
            onValueChange={setIsOnline}
            trackColor={{ false: '#CBD5E1', true: '#94E0D1' }}
            thumbColor={isOnline ? '#400022' : '#F1F5F9'}
          />
        </View>

        {/* Key Metrics Grid */}
        <View style={styles.metricsGrid}>
          <View style={styles.metricCard}>
            <Ionicons name="cash-outline" size={24} color="#400022" />
            <Text style={styles.metricValue}>KSh 38,250</Text>
            <Text style={styles.metricLabel}>Net Payout Ready</Text>
          </View>
          <View style={styles.metricCard}>
            <Ionicons name="calendar-outline" size={24} color="#400022" />
            <Text style={styles.metricValue}>28</Text>
            <Text style={styles.metricLabel}>Completed Shifts</Text>
          </View>
          <View style={styles.metricCard}>
            <Ionicons name="star" size={24} color="#F59E0B" />
            <Text style={styles.metricValue}>4.9 ★</Text>
            <Text style={styles.metricLabel}>Client Rating</Text>
          </View>
          <View style={styles.metricCard}>
            <Ionicons name="checkmark-done-circle-outline" size={24} color="#400022" />
            <Text style={styles.metricValue}>96%</Text>
            <Text style={styles.metricLabel}>Acceptance Rate</Text>
          </View>
        </View>

        {/* Pending Requests Section */}
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <Text style={styles.sectionTitle}>Incoming Requests</Text>
            <View style={styles.countBadge}>
              <Text style={styles.countText}>{pendingBookings.length}</Text>
            </View>
          </View>

          {pendingBookings.length === 0 ? (
            <View style={styles.emptyCard}>
              <Ionicons name="checkmark-circle-outline" size={36} color="#10B981" />
              <Text style={styles.emptyText}>All care requests handled! No pending requests.</Text>
            </View>
          ) : (
            pendingBookings.map((req) => (
              <View key={req.id} style={styles.requestCard}>
                <View style={styles.requestTop}>
                  <View>
                    <Text style={styles.clientName}>{req.customerName}</Text>
                    <Text style={styles.serviceName}>{req.serviceName}</Text>
                  </View>
                  <Text style={styles.requestPrice}>KSh {req.agreedRate.toLocaleString()}</Text>
                </View>

                <View style={styles.detailRow}>
                  <Ionicons name="time-outline" size={15} color="#64748B" />
                  <Text style={styles.detailText}>{req.startDate} • {req.timeSlot}</Text>
                </View>
                <View style={styles.detailRow}>
                  <Ionicons name="location-outline" size={15} color="#64748B" />
                  <Text style={styles.detailText}>{req.location}</Text>
                </View>
                {req.careNotes ? (
                  <View style={styles.notesBox}>
                    <Text style={styles.notesLabel}>Care Notes:</Text>
                    <Text style={styles.notesText}>{req.careNotes}</Text>
                  </View>
                ) : null}

                <View style={styles.actionRow}>
                  <TouchableOpacity
                    style={[styles.actionBtn, styles.declineBtn]}
                    onPress={() => handleDecline(req.id)}
                  >
                    <Text style={styles.declineText}>Decline</Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={[styles.actionBtn, styles.acceptBtn]}
                    onPress={() => handleAccept(req.id)}
                  >
                    <Text style={styles.acceptText}>Accept Shift</Text>
                  </TouchableOpacity>
                </View>
              </View>
            ))
          )}
        </View>

        {/* Upcoming Confirmed Shifts */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Upcoming Confirmed Shifts</Text>
          {upcomingBookings.length === 0 ? (
            <Text style={styles.emptySubText}>No active or upcoming shifts.</Text>
          ) : (
            upcomingBookings.map((b) => (
              <View key={b.id} style={styles.upcomingCard}>
                <View style={styles.upcomingHeader}>
                  <Text style={styles.upcomingClient}>{b.customerName}</Text>
                  <View style={styles.statusPill}>
                    <Text style={styles.statusPillText}>{b.status}</Text>
                  </View>
                </View>
                <Text style={styles.upcomingService}>{b.serviceName}</Text>
                <Text style={styles.upcomingTime}>
                  {b.startDate} ({b.timeSlot}) • {b.location}
                </Text>
              </View>
            ))
          )}
        </View>
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
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingTop: 54,
    paddingBottom: 16,
    paddingHorizontal: 20,
    backgroundColor: '#FFFFFF',
    borderBottomWidth: 1,
    borderBottomColor: '#E2E8F0',
  },
  greeting: {
    fontSize: 13,
    color: '#64748B',
    fontWeight: '500',
  },
  specialistName: {
    fontSize: 20,
    fontWeight: '800',
    color: '#0F172A',
    marginTop: 2,
  },
  badgeRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 4,
  },
  badgeText: {
    fontSize: 11,
    color: '#400022',
    fontWeight: '600',
    marginLeft: 4,
  },
  avatarPlaceholder: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: '#FBE8F0',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: '#400022',
  },
  avatarLetter: {
    fontSize: 16,
    fontWeight: '700',
    color: '#400022',
  },
  availabilityCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    marginHorizontal: 16,
    marginTop: 16,
    padding: 16,
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  availInfo: {
    flex: 1,
    marginRight: 10,
  },
  statusRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  statusDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    marginRight: 8,
  },
  availTitle: {
    fontSize: 14,
    fontWeight: '700',
    color: '#0F172A',
  },
  availSubtitle: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 3,
  },
  metricsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    paddingHorizontal: 12,
    marginTop: 12,
  },
  metricCard: {
    width: '46%',
    backgroundColor: '#FFFFFF',
    margin: '2%',
    padding: 14,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  metricValue: {
    fontSize: 18,
    fontWeight: '800',
    color: '#0F172A',
    marginTop: 8,
  },
  metricLabel: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 2,
  },
  section: {
    marginTop: 16,
    paddingHorizontal: 16,
  },
  sectionHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#0F172A',
  },
  countBadge: {
    backgroundColor: '#400022',
    borderRadius: 10,
    paddingHorizontal: 8,
    paddingVertical: 2,
    marginLeft: 8,
  },
  countText: {
    color: '#FFFFFF',
    fontSize: 11,
    fontWeight: '700',
  },
  emptyCard: {
    backgroundColor: '#FFFFFF',
    padding: 20,
    borderRadius: 12,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  emptyText: {
    fontSize: 13,
    color: '#64748B',
    marginTop: 8,
  },
  requestCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    padding: 16,
    marginBottom: 12,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  requestTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 8,
  },
  clientName: {
    fontSize: 16,
    fontWeight: '700',
    color: '#0F172A',
  },
  serviceName: {
    fontSize: 13,
    color: '#400022',
    fontWeight: '600',
    marginTop: 2,
  },
  requestPrice: {
    fontSize: 16,
    fontWeight: '700',
    color: '#400022',
  },
  detailRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 4,
  },
  detailText: {
    fontSize: 13,
    color: '#475569',
    marginLeft: 6,
  },
  notesBox: {
    backgroundColor: '#F8FAFC',
    padding: 10,
    borderRadius: 8,
    marginTop: 10,
    borderLeftWidth: 3,
    borderLeftColor: '#400022',
  },
  notesLabel: {
    fontSize: 11,
    fontWeight: '700',
    color: '#64748B',
  },
  notesText: {
    fontSize: 12,
    color: '#334155',
    marginTop: 2,
  },
  actionRow: {
    flexDirection: 'row',
    marginTop: 14,
    gap: 10,
  },
  actionBtn: {
    flex: 1,
    paddingVertical: 10,
    borderRadius: 8,
    alignItems: 'center',
  },
  declineBtn: {
    backgroundColor: '#F1F5F9',
  },
  declineText: {
    color: '#64748B',
    fontWeight: '600',
    fontSize: 13,
  },
  acceptBtn: {
    backgroundColor: '#400022',
  },
  acceptText: {
    color: '#FFFFFF',
    fontWeight: '700',
    fontSize: 13,
  },
  emptySubText: {
    fontSize: 13,
    color: '#94A3B8',
    marginTop: 4,
  },
  upcomingCard: {
    backgroundColor: '#FFFFFF',
    padding: 14,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: '#E2E8F0',
    marginTop: 8,
  },
  upcomingHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  upcomingClient: {
    fontSize: 14,
    fontWeight: '700',
    color: '#0F172A',
  },
  statusPill: {
    backgroundColor: '#ECFDF5',
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
  },
  statusPillText: {
    color: '#065F46',
    fontSize: 11,
    fontWeight: '700',
  },
  upcomingService: {
    fontSize: 13,
    color: '#400022',
    fontWeight: '600',
    marginTop: 4,
  },
  upcomingTime: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 4,
  },
});
