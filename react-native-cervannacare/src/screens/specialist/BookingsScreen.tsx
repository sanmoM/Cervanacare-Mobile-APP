import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  Linking,
  Alert,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { INITIAL_BOOKINGS } from '../../services/mockData';
import { Booking } from '../../types';

export const SpecialistBookingsScreen: React.FC = () => {
  const [bookings, setBookings] = useState<Booking[]>(INITIAL_BOOKINGS);
  const [selectedFilter, setSelectedFilter] = useState<string>('ALL');

  const filterTabs = ['ALL', 'CONFIRMED', 'PENDING', 'COMPLETED'];

  const filteredBookings = bookings.filter((b) => {
    if (selectedFilter === 'ALL') return true;
    return b.status === selectedFilter;
  });

  const handleCallClient = (phone?: string) => {
    const targetPhone = phone || '+254712345678';
    Linking.openURL(`tel:${targetPhone}`);
  };

  const handleCompleteShift = (bookingId: string) => {
    Alert.alert(
      'Complete Care Shift',
      'Confirm that you have completed this patient care visit according to protocol. Net earnings will be credited to your M-Pesa balance.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Confirm Completion',
          onPress: () => {
            setBookings((prev) =>
              prev.map((b) => (b.id === bookingId ? { ...b, status: 'COMPLETED' } : b))
            );
            Alert.alert('Shift Completed', 'Earnings credited. Client prompted to leave a review.');
          },
        },
      ]
    );
  };

  const renderBookingItem = ({ item }: { item: Booking }) => {
    const isCompleted = item.status === 'COMPLETED';
    const isConfirmed = item.status === 'CONFIRMED' || item.status === 'IN_PROGRESS';

    return (
      <View style={styles.card}>
        <View style={styles.cardHeader}>
          <View>
            <Text style={styles.customerName}>{item.customerName}</Text>
            <Text style={styles.serviceName}>{item.serviceName}</Text>
          </View>
          <View
            style={[
              styles.statusBadge,
              isCompleted
                ? styles.statusCompleted
                : isConfirmed
                ? styles.statusConfirmed
                : styles.statusPending,
            ]}
          >
            <Text
              style={[
                styles.statusText,
                isCompleted
                  ? styles.textCompleted
                  : isConfirmed
                  ? styles.textConfirmed
                  : styles.textPending,
              ]}
            >
              {item.status}
            </Text>
          </View>
        </View>

        <View style={styles.detailsBox}>
          <View style={styles.row}>
            <Ionicons name="calendar-outline" size={15} color="#64748B" />
            <Text style={styles.detailText}>{item.startDate} ({item.timeSlot})</Text>
          </View>
          <View style={styles.row}>
            <Ionicons name="location-outline" size={15} color="#64748B" />
            <Text style={styles.detailText}>{item.location}</Text>
          </View>
          <View style={styles.row}>
            <Ionicons name="cash-outline" size={15} color="#64748B" />
            <Text style={styles.detailText}>Agreed Fee: KSh {item.agreedRate.toLocaleString()}</Text>
          </View>
          {item.careNotes ? (
            <View style={styles.notesBox}>
              <Text style={styles.notesLabel}>Patient Care Requirement:</Text>
              <Text style={styles.notesText}>{item.careNotes}</Text>
            </View>
          ) : null}
        </View>

        {/* Action Buttons */}
        <View style={styles.actionRow}>
          <TouchableOpacity
            style={styles.callButton}
            onPress={() => handleCallClient(item.customerPhone)}
          >
            <Ionicons name="call-outline" size={16} color="#0D7A68" style={{ marginRight: 6 }} />
            <Text style={styles.callButtonText}>Call Client</Text>
          </TouchableOpacity>

          {isConfirmed && (
            <TouchableOpacity
              style={styles.completeButton}
              onPress={() => handleCompleteShift(item.id)}
            >
              <Ionicons name="checkmark-done" size={16} color="#FFFFFF" style={{ marginRight: 6 }} />
              <Text style={styles.completeButtonText}>Finish Shift</Text>
            </TouchableOpacity>
          )}
        </View>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>My Care Shifts</Text>
        <Text style={styles.subtitle}>Manage bookings, patient visits, and shift completions</Text>
      </View>

      {/* Filter Tabs */}
      <View style={styles.tabsRow}>
        {filterTabs.map((tab) => (
          <TouchableOpacity
            key={tab}
            style={[styles.tabButton, selectedFilter === tab && styles.tabButtonActive]}
            onPress={() => setSelectedFilter(tab)}
          >
            <Text style={[styles.tabText, selectedFilter === tab && styles.tabTextActive]}>
              {tab}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      {filteredBookings.length === 0 ? (
        <View style={styles.emptyContainer}>
          <Ionicons name="calendar-clear-outline" size={60} color="#CBD5E1" />
          <Text style={styles.emptyTitle}>No Shifts in this Category</Text>
          <Text style={styles.emptySubtitle}>
            When families book your care services, their shifts will appear here.
          </Text>
        </View>
      ) : (
        <FlatList
          data={filteredBookings}
          keyExtractor={(item) => item.id}
          renderItem={renderBookingItem}
          contentContainerStyle={styles.listContent}
          showsVerticalScrollIndicator={false}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F8FAFC',
  },
  header: {
    paddingTop: 54,
    paddingBottom: 16,
    paddingHorizontal: 20,
    backgroundColor: '#FFFFFF',
    borderBottomWidth: 1,
    borderBottomColor: '#E2E8F0',
  },
  title: {
    fontSize: 22,
    fontWeight: '800',
    color: '#0F172A',
  },
  subtitle: {
    fontSize: 13,
    color: '#64748B',
    marginTop: 2,
  },
  tabsRow: {
    flexDirection: 'row',
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#E2E8F0',
    gap: 8,
  },
  tabButton: {
    paddingHorizontal: 14,
    paddingVertical: 6,
    borderRadius: 20,
    backgroundColor: '#F1F5F9',
  },
  tabButtonActive: {
    backgroundColor: '#0D7A68',
  },
  tabText: {
    fontSize: 12,
    fontWeight: '600',
    color: '#64748B',
  },
  tabTextActive: {
    color: '#FFFFFF',
  },
  listContent: {
    padding: 16,
    gap: 12,
  },
  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    padding: 16,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
  },
  customerName: {
    fontSize: 16,
    fontWeight: '700',
    color: '#0F172A',
  },
  serviceName: {
    fontSize: 13,
    color: '#0D7A68',
    fontWeight: '600',
    marginTop: 2,
  },
  statusBadge: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 6,
  },
  statusCompleted: {
    backgroundColor: '#ECFDF5',
  },
  statusConfirmed: {
    backgroundColor: '#EFF6FF',
  },
  statusPending: {
    backgroundColor: '#FEF3C7',
  },
  statusText: {
    fontSize: 11,
    fontWeight: '700',
  },
  textCompleted: {
    color: '#065F46',
  },
  textConfirmed: {
    color: '#1D4ED8',
  },
  textPending: {
    color: '#B45309',
  },
  detailsBox: {
    marginTop: 12,
    backgroundColor: '#F8FAFC',
    padding: 12,
    borderRadius: 10,
    gap: 6,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  detailText: {
    fontSize: 13,
    color: '#334155',
    marginLeft: 8,
  },
  notesBox: {
    borderTopWidth: 1,
    borderTopColor: '#E2E8F0',
    paddingTop: 8,
    marginTop: 4,
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
  callButton: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#E6F4F1',
    paddingVertical: 10,
    borderRadius: 8,
  },
  callButtonText: {
    fontSize: 13,
    fontWeight: '700',
    color: '#0D7A68',
  },
  completeButton: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#0D7A68',
    paddingVertical: 10,
    borderRadius: 8,
  },
  completeButtonText: {
    fontSize: 13,
    fontWeight: '700',
    color: '#FFFFFF',
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 40,
  },
  emptyTitle: {
    fontSize: 18,
    fontWeight: '700',
    color: '#0F172A',
    marginTop: 16,
  },
  emptySubtitle: {
    fontSize: 14,
    color: '#64748B',
    textAlign: 'center',
    marginTop: 8,
    lineHeight: 20,
  },
});
