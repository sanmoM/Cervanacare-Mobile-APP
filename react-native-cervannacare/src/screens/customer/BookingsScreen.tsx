import React, { useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet, Alert } from 'react-native';
import { INITIAL_BOOKINGS } from '../../services/mockData';
import { Booking } from '../../types';
import MpesaModal from '../../components/MpesaModal';

export default function BookingsScreen() {
  const [bookings, setBookings] = useState<Booking[]>(INITIAL_BOOKINGS);
  const [filter, setFilter] = useState<'ALL' | 'PENDING' | 'CONFIRMED' | 'COMPLETED'>('ALL');
  const [mpesaTarget, setMpesaTarget] = useState<Booking | null>(null);

  const filtered = bookings.filter((b) => {
    if (filter === 'ALL') return true;
    return b.status === filter;
  });

  const handleMpesaSuccess = (receipt: string) => {
    if (!mpesaTarget) return;
    setBookings((prev) =>
      prev.map((b) =>
        b.id === mpesaTarget.id
          ? { ...b, paymentStatus: 'PAID', paymentMethod: `M-Pesa (${receipt})` }
          : b
      )
    );
  };

  const handleCancel = (bookingId: string) => {
    Alert.alert('Cancel Booking', 'Are you sure you want to cancel this care booking?', [
      { text: 'Keep', style: 'cancel' },
      {
        text: 'Yes, Cancel',
        style: 'destructive',
        onPress: () => {
          setBookings((prev) =>
            prev.map((b) => (b.id === bookingId ? { ...b, status: 'CANCELLED' } : b))
          );
        },
      },
    ]);
  };

  return (
    <View style={styles.container}>
      {/* Filter Tabs */}
      <View style={styles.tabRow}>
        {(['ALL', 'PENDING', 'CONFIRMED', 'COMPLETED'] as const).map((t) => (
          <TouchableOpacity
            key={t}
            style={[styles.tab, filter === t && styles.tabActive]}
            onPress={() => setFilter(t)}
          >
            <Text style={[styles.tabText, filter === t && styles.tabTextActive]}>{t}</Text>
          </TouchableOpacity>
        ))}
      </View>

      <ScrollView contentContainerStyle={{ padding: 16, paddingBottom: 40 }}>
        {filtered.map((item) => (
          <View key={item.id} style={styles.card}>
            <View style={styles.cardHeader}>
              <Text style={styles.cardId}>#{item.id}</Text>
              <View
                style={[
                  styles.badge,
                  item.status === 'CONFIRMED'
                    ? styles.badgeConfirmed
                    : item.status === 'PENDING'
                    ? styles.badgePending
                    : styles.badgeDefault,
                ]}
              >
                <Text
                  style={[
                    styles.badgeText,
                    item.status === 'CONFIRMED'
                      ? styles.badgeTextConfirmed
                      : item.status === 'PENDING'
                      ? styles.badgeTextPending
                      : styles.badgeTextDefault,
                  ]}
                >
                  {item.status}
                </Text>
              </View>
            </View>

            <Text style={styles.serviceName}>{item.serviceName}</Text>
            <Text style={styles.specialist}>Specialist: {item.specialistName}</Text>
            <Text style={styles.details}>
              📅 {item.startDate} • {item.shiftType} ({item.durationDays} Days)
            </Text>
            <Text style={styles.details}>📍 {item.location}</Text>

            <View style={styles.paymentRow}>
              <Text style={styles.quoteText}>Quote: KSh {item.quoteKsh}</Text>
              <Text
                style={[
                  styles.payStatus,
                  item.paymentStatus === 'PAID' ? styles.payPaid : styles.payUnpaid,
                ]}
              >
                {item.paymentStatus === 'PAID' ? 'LIPA NA M-PESA: PAID' : 'UNPAID'}
              </Text>
            </View>

            <View style={styles.actions}>
              {item.paymentStatus === 'UNPAID' && item.status !== 'CANCELLED' && (
                <TouchableOpacity style={styles.payBtn} onPress={() => setMpesaTarget(item)}>
                  <Text style={styles.payBtnText}>Pay via M-Pesa</Text>
                </TouchableOpacity>
              )}
              {item.status !== 'CANCELLED' && item.status !== 'COMPLETED' && (
                <TouchableOpacity style={styles.cancelBtn} onPress={() => handleCancel(item.id)}>
                  <Text style={styles.cancelBtnText}>Cancel</Text>
                </TouchableOpacity>
              )}
            </View>
          </View>
        ))}
      </ScrollView>

      {mpesaTarget && (
        <MpesaModal
          visible={!!mpesaTarget}
          amountKsh={mpesaTarget.quoteKsh}
          bookingId={mpesaTarget.id}
          onSuccess={handleMpesaSuccess}
          onClose={() => setMpesaTarget(null)}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8FAFC' },
  tabRow: { flexDirection: 'row', backgroundColor: '#FFFFFF', padding: 8, borderBottomWidth: 1, borderBottomColor: '#E2E8F0' },
  tab: { flex: 1, paddingVertical: 8, alignItems: 'center', borderRadius: 8 },
  tabActive: { backgroundColor: '#F0FDFA' },
  tabText: { fontSize: 11, fontWeight: '600', color: '#64748B' },
  tabTextActive: { color: '#0D7A68', fontWeight: 'bold' },
  card: { backgroundColor: '#FFFFFF', borderRadius: 14, padding: 14, marginBottom: 12, borderWidth: 1, borderColor: '#E2E8F0' },
  cardHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 },
  cardId: { fontSize: 13, fontWeight: 'bold', color: '#0D7A68' },
  badge: { paddingHorizontal: 8, paddingVertical: 2, borderRadius: 4 },
  badgeConfirmed: { backgroundColor: '#CCFBF1' },
  badgePending: { backgroundColor: '#FEF3C7' },
  badgeDefault: { backgroundColor: '#F1F5F9' },
  badgeText: { fontSize: 10, fontWeight: 'bold' },
  badgeTextConfirmed: { color: '#0F766E' },
  badgeTextPending: { color: '#B45309' },
  badgeTextDefault: { color: '#475569' },
  serviceName: { fontSize: 15, fontWeight: 'bold', color: '#0F172A' },
  specialist: { fontSize: 12, color: '#334155', marginTop: 2 },
  details: { fontSize: 11, color: '#64748B', marginTop: 2 },
  paymentRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginTop: 10, paddingTop: 8, borderTopWidth: 1, borderTopColor: '#F1F5F9' },
  quoteText: { fontSize: 13, fontWeight: 'bold', color: '#0F172A' },
  payStatus: { fontSize: 10, fontWeight: 'bold' },
  payPaid: { color: '#009639' },
  payUnpaid: { color: '#D97706' },
  actions: { flexDirection: 'row', justifyContent: 'flex-end', gap: 8, marginTop: 10 },
  payBtn: { backgroundColor: '#009639', paddingVertical: 6, paddingHorizontal: 12, borderRadius: 8 },
  payBtnText: { color: '#FFFFFF', fontSize: 11, fontWeight: 'bold' },
  cancelBtn: { paddingVertical: 6, paddingHorizontal: 10 },
  cancelBtnText: { color: '#DC2626', fontSize: 11 },
});
