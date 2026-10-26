import React, { useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet, FlatList } from 'react-native';
import { INITIAL_SPECIALISTS, INITIAL_BOOKINGS } from '../../services/mockData';
import { ServiceCategory } from '../../types';

const SERVICES: ServiceCategory[] = [
  'Pre & Post Pregnancy Care',
  'Post Surgery Care',
  'Elderly Care',
  'Physiotherapy',
  'Nanny & Housekeeping',
  'Special Needs Care',
  'Palliative Care',
];

export default function CustomerHomeScreen({ navigation }: any) {
  const [selectedLocation, setSelectedLocation] = useState('Nairobi - Eastern Bypass');

  return (
    <ScrollView style={styles.container} contentContainerStyle={{ paddingBottom: 30 }}>
      {/* Location Bar */}
      <View style={styles.locationBar}>
        <Text style={styles.locationLabel}>📍 Care in: </Text>
        <Text style={styles.locationVal}>{selectedLocation}</Text>
      </View>

      {/* Hero Banner */}
      <View style={styles.heroCard}>
        <Text style={styles.heroTag}>KENYA'S VETTED CARE MARKETPLACE</Text>
        <Text style={styles.heroTitle}>Trusted Nurses, Caregivers & House Managers</Text>
        <Text style={styles.heroDesc}>
          Licensed by regulatory councils & background checked by DCI Kenya.
        </Text>
      </View>

      {/* 7 Core Services */}
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionTitle}>Specialized Services</Text>
        <Text style={styles.seeAll} onPress={() => navigation.navigate('Explore')}>
          View All (7)
        </Text>
      </View>

      <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.servicesScroll}>
        {SERVICES.map((item, idx) => (
          <TouchableOpacity
            key={idx}
            style={styles.serviceCard}
            onPress={() => navigation.navigate('Explore', { selectedService: item })}
          >
            <View style={styles.serviceIconCircle}>
              <Text style={{ fontSize: 18 }}>🩺</Text>
            </View>
            <Text style={styles.serviceCardText}>{item}</Text>
          </TouchableOpacity>
        ))}
      </ScrollView>

      {/* Featured Specialists */}
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionTitle}>Featured Specialists</Text>
        <Text style={styles.seeAll} onPress={() => navigation.navigate('Explore')}>
          See All
        </Text>
      </View>

      {INITIAL_SPECIALISTS.map((spec) => (
        <TouchableOpacity
          key={spec.id}
          style={styles.specCard}
          onPress={() => navigation.navigate('SpecialistDetail', { specialist: spec })}
        >
          <View style={styles.specHeader}>
            <View style={styles.specAvatar}>
              <Text style={styles.specAvatarText}>{spec.name.slice(0, 2).toUpperCase()}</Text>
            </View>
            <View style={{ flex: 1 }}>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 4 }}>
                <Text style={styles.specName}>{spec.name}</Text>
                <Text style={{ color: '#400022' }}>✓</Text>
              </View>
              <Text style={styles.specTitle}>{spec.title}</Text>
              <Text style={styles.specMeta}>
                ★ {spec.rating} ({spec.reviewCount}) • {spec.experienceYears} yrs exp
              </Text>
            </View>
          </View>

          <View style={styles.specFooter}>
            <Text style={styles.specRate}>From KSh {spec.dailyRateKsh} / day</Text>
            <TouchableOpacity
              style={styles.bookBtn}
              onPress={() => navigation.navigate('SpecialistDetail', { specialist: spec })}
            >
              <Text style={styles.bookBtnText}>Book Now</Text>
            </TouchableOpacity>
          </View>
        </TouchableOpacity>
      ))}

      {/* 24/7 Hotline */}
      <View style={styles.hotlineCard}>
        <Text style={styles.hotlineTitle}>Need Immediate Home Care?</Text>
        <Text style={styles.hotlineDesc}>
          Our Eastern Bypass coordination hub dispatches same-day nurses & aides: +254 700 225533
        </Text>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8FAFC' },
  locationBar: {
    flexDirection: 'row',
    paddingHorizontal: 16,
    paddingVertical: 10,
    backgroundColor: '#FFFFFF',
    borderBottomWidth: 1,
    borderBottomColor: '#E2E8F0',
  },
  locationLabel: { fontSize: 12, color: '#64748B' },
  locationVal: { fontSize: 12, fontWeight: 'bold', color: '#0F172A' },
  heroCard: {
    backgroundColor: '#400022',
    margin: 16,
    borderRadius: 18,
    padding: 18,
  },
  heroTag: { color: '#FEF3C7', fontSize: 10, fontWeight: 'bold', letterSpacing: 0.8 },
  heroTitle: { color: '#FFFFFF', fontSize: 18, fontWeight: 'bold', marginVertical: 6 },
  heroDesc: { color: '#E2E8F0', fontSize: 12 },
  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    marginTop: 10,
    marginBottom: 8,
  },
  sectionTitle: { fontSize: 16, fontWeight: 'bold', color: '#0F172A' },
  seeAll: { fontSize: 12, fontWeight: '600', color: '#400022' },
  servicesScroll: { paddingLeft: 16, marginBottom: 16 },
  serviceCard: {
    width: 120,
    height: 110,
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    padding: 12,
    marginRight: 10,
    justifyContent: 'space-between',
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  serviceIconCircle: {
    width: 34,
    height: 34,
    borderRadius: 8,
    backgroundColor: '#FBE8F0',
    justifyContent: 'center',
    alignItems: 'center',
  },
  serviceCardText: { fontSize: 11, fontWeight: 'bold', color: '#0F172A', lineHeight: 14 },
  specCard: {
    backgroundColor: '#FFFFFF',
    marginHorizontal: 16,
    marginBottom: 10,
    borderRadius: 14,
    padding: 14,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  specHeader: { flexDirection: 'row', gap: 12 },
  specAvatar: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: '#400022',
    justifyContent: 'center',
    alignItems: 'center',
  },
  specAvatarText: { color: '#FFFFFF', fontWeight: 'bold', fontSize: 16 },
  specName: { fontSize: 14, fontWeight: 'bold', color: '#0F172A' },
  specTitle: { fontSize: 11, color: '#64748B', marginTop: 1 },
  specMeta: { fontSize: 11, color: '#400022', marginTop: 3 },
  specFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 12,
    paddingTop: 10,
    borderTopWidth: 1,
    borderTopColor: '#F1F5F9',
  },
  specRate: { fontSize: 13, fontWeight: 'bold', color: '#400022' },
  bookBtn: { backgroundColor: '#400022', borderRadius: 8, paddingVertical: 6, paddingHorizontal: 14 },
  bookBtnText: { color: '#FFFFFF', fontSize: 12, fontWeight: 'bold' },
  hotlineCard: {
    backgroundColor: '#FEF3C7',
    marginHorizontal: 16,
    marginTop: 10,
    borderRadius: 14,
    padding: 14,
  },
  hotlineTitle: { fontSize: 13, fontWeight: 'bold', color: '#78350F' },
  hotlineDesc: { fontSize: 11, color: '#92400E', marginTop: 4 },
});
