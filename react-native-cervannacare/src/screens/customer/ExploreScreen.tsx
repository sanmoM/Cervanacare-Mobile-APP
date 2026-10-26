import React, { useState } from 'react';
import { View, Text, TextInput, ScrollView, TouchableOpacity, StyleSheet } from 'react-native';
import { INITIAL_SPECIALISTS } from '../../services/mockData';
import { ServiceCategory } from '../../types';

export default function ExploreScreen({ route, navigation }: any) {
  const [search, setSearch] = useState('');
  const [activeCategory, setActiveCategory] = useState<ServiceCategory | null>(
    route?.params?.selectedService || null
  );

  const categories: ServiceCategory[] = [
    'Pre & Post Pregnancy Care',
    'Post Surgery Care',
    'Elderly Care',
    'Physiotherapy',
    'Nanny & Housekeeping',
    'Special Needs Care',
    'Palliative Care',
  ];

  const filtered = INITIAL_SPECIALISTS.filter((item) => {
    const matchSearch =
      search === '' ||
      item.name.toLowerCase().includes(search.toLowerCase()) ||
      item.title.toLowerCase().includes(search.toLowerCase()) ||
      item.skills.some((s) => s.toLowerCase().includes(search.toLowerCase()));

    const matchCat = !activeCategory || item.serviceCategories.includes(activeCategory);
    return matchSearch && matchCat;
  });

  return (
    <View style={styles.container}>
      {/* Search Input */}
      <View style={styles.searchBarContainer}>
        <TextInput
          style={styles.searchInput}
          placeholder="Search by nurse, physiotherapist, nanny, or skill..."
          placeholderTextColor="#ddd"
          value={search}
          onChangeText={setSearch}
        />
      </View>

      {/* Filter Chips */}
      <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.chipsScroll}>
        <TouchableOpacity
          style={[styles.chip, !activeCategory && styles.chipActive]}
          onPress={() => setActiveCategory(null)}
        >
          <Text style={[styles.chipText, !activeCategory && styles.chipTextActive]}>All Services</Text>
        </TouchableOpacity>
        {categories.map((cat, idx) => (
          <TouchableOpacity
            key={idx}
            style={[styles.chip, activeCategory === cat && styles.chipActive]}
            onPress={() => setActiveCategory(activeCategory === cat ? null : cat)}
          >
            <Text style={[styles.chipText, activeCategory === cat && styles.chipTextActive]}>{cat}</Text>
          </TouchableOpacity>
        ))}
      </ScrollView>

      {/* Results List */}
      <ScrollView contentContainerStyle={{ padding: 16, paddingBottom: 40 }}>
        <Text style={styles.countText}>{filtered.length} Specialists Available</Text>
        {filtered.map((spec) => (
          <TouchableOpacity
            key={spec.id}
            style={styles.card}
            onPress={() => navigation.navigate('SpecialistDetail', { specialist: spec })}
          >
            <View style={{ flexDirection: 'row', gap: 12 }}>
              <View style={styles.avatar}>
                <Text style={styles.avatarText}>{spec.name.slice(0, 2).toUpperCase()}</Text>
              </View>
              <View style={{ flex: 1 }}>
                <Text style={styles.name}>{spec.name} ✓</Text>
                <Text style={styles.title}>{spec.title}</Text>
                <Text style={styles.meta}>
                  ★ {spec.rating} ({spec.reviewCount}) • {spec.experienceYears} yrs exp
                </Text>
                <Text style={styles.locations}>Areas: {spec.locations.slice(0, 3).join(', ')}</Text>
              </View>
            </View>

            <View style={styles.footer}>
              <Text style={styles.rate}>KSh {spec.dailyRateKsh} / day</Text>
              <TouchableOpacity
                style={styles.bookBtn}
                onPress={() => navigation.navigate('SpecialistDetail', { specialist: spec })}
              >
                <Text style={styles.bookBtnText}>View & Book</Text>
              </TouchableOpacity>
            </View>
          </TouchableOpacity>
        ))}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8FAFC' },
  searchBarContainer: { padding: 12, backgroundColor: '#FFFFFF', borderBottomWidth: 1, borderBottomColor: '#E2E8F0' },
  searchInput: { backgroundColor: '#F1F5F9', borderRadius: 10, paddingHorizontal: 12, paddingVertical: 8, fontSize: 13 },
  chipsScroll: { paddingHorizontal: 12, paddingVertical: 8, backgroundColor: '#FFFFFF', maxHeight: 48 },
  chip: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#400022',
    marginRight: 8,
  },
  chipActive: { backgroundColor: '#400022' },
  chipText: { fontSize: 11, color: '#400022', fontWeight: '600' },
  chipTextActive: { color: '#FFFFFF', fontWeight: 'bold' },
  countText: { fontSize: 12, fontWeight: '600', color: '#64748B', marginBottom: 10 },
  card: { backgroundColor: '#FFFFFF', borderRadius: 14, padding: 14, marginBottom: 12, borderWidth: 1, borderColor: '#E2E8F0' },
  avatar: { width: 44, height: 44, borderRadius: 22, backgroundColor: '#400022', justifyContent: 'center', alignItems: 'center' },
  avatarText: { color: '#FFFFFF', fontWeight: 'bold' },
  name: { fontSize: 14, fontWeight: 'bold', color: '#0F172A' },
  title: { fontSize: 11, color: '#64748B', marginTop: 1 },
  meta: { fontSize: 11, color: '#400022', marginTop: 2 },
  locations: { fontSize: 10, color: '#94A3B8', marginTop: 2 },
  footer: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginTop: 10, paddingTop: 10, borderTopWidth: 1, borderTopColor: '#F1F5F9' },
  rate: { fontSize: 13, fontWeight: 'bold', color: '#400022' },
  bookBtn: { backgroundColor: '#400022', borderRadius: 8, paddingVertical: 6, paddingHorizontal: 12 },
  bookBtnText: { color: '#FFFFFF', fontSize: 11, fontWeight: 'bold' },
});
