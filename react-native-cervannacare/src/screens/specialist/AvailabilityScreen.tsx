import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Switch,
  Alert,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';

export const SpecialistAvailabilityScreen: React.FC = () => {
  const [activeDays, setActiveDays] = useState<string[]>([
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
  ]);

  const [shifts, setShifts] = useState({
    dayShift: true,
    nightShift: false,
    liveIn: false,
    emergencyCalls: true,
  });

  const [selectedLocations, setSelectedLocations] = useState<string[]>([
    'Eastern Bypass',
    'Nairobi',
    'Kiambu',
  ]);

  const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  const locationsList = ['Nairobi', 'Eastern Bypass', 'Kiambu', 'Mombasa', 'Nakuru', 'Kisumu', 'Machakos'];

  const toggleDay = (day: string) => {
    if (activeDays.includes(day)) {
      if (activeDays.length === 1) {
        Alert.alert('Notice', 'You must be available at least one day per week.');
        return;
      }
      setActiveDays(activeDays.filter((d) => d !== day));
    } else {
      setActiveDays([...activeDays, day]);
    }
  };

  const toggleLocation = (loc: string) => {
    if (selectedLocations.includes(loc)) {
      if (selectedLocations.length === 1) {
        Alert.alert('Notice', 'Select at least one operating location in Kenya.');
        return;
      }
      setSelectedLocations(selectedLocations.filter((l) => l !== loc));
    } else {
      setSelectedLocations([...selectedLocations, loc]);
    }
  };

  const handleSave = () => {
    Alert.alert('Availability Saved', 'Your schedule and operating areas have been updated across Cervanna Care.');
  };

  return (
    <View style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scrollContent}>
        <View style={styles.header}>
          <Text style={styles.title}>Work Availability & Roster</Text>
          <Text style={styles.subtitle}>Configure which days, shifts, and towns you accept care assignments</Text>
        </View>

        {/* Days of Week */}
        <View style={styles.card}>
          <Text style={styles.sectionTitle}>Working Days</Text>
          <Text style={styles.sectionSubtitle}>Tap days when you are open for bookings</Text>
          <View style={styles.daysRow}>
            {daysOfWeek.map((day) => {
              const isActive = activeDays.includes(day);
              return (
                <TouchableOpacity
                  key={day}
                  style={[styles.dayChip, isActive && styles.dayChipActive]}
                  onPress={() => toggleDay(day)}
                >
                  <Text style={[styles.dayText, isActive && styles.dayTextActive]}>
                    {day.substring(0, 3)}
                  </Text>
                </TouchableOpacity>
              );
            })}
          </View>
        </View>

        {/* Shift Formats */}
        <View style={styles.card}>
          <Text style={styles.sectionTitle}>Preferred Shift Arrangements</Text>

          <View style={styles.switchRow}>
            <View style={styles.switchTextWrap}>
              <Text style={styles.switchTitle}>Day Care (8:00 AM – 5:00 PM)</Text>
              <Text style={styles.switchSubtitle}>Standard home nursing or elderly day assistance</Text>
            </View>
            <Switch
              value={shifts.dayShift}
              onValueChange={(val) => setShifts({ ...shifts, dayShift: val })}
              trackColor={{ false: '#CBD5E1', true: '#94E0D1' }}
              thumbColor={shifts.dayShift ? '#0D7A68' : '#F1F5F9'}
            />
          </View>

          <View style={styles.switchRow}>
            <View style={styles.switchTextWrap}>
              <Text style={styles.switchTitle}>Night Shift (6:00 PM – 6:00 AM)</Text>
              <Text style={styles.switchSubtitle}>Overnight infant care or post-surgery patient monitoring</Text>
            </View>
            <Switch
              value={shifts.nightShift}
              onValueChange={(val) => setShifts({ ...shifts, nightShift: val })}
              trackColor={{ false: '#CBD5E1', true: '#94E0D1' }}
              thumbColor={shifts.nightShift ? '#0D7A68' : '#F1F5F9'}
            />
          </View>

          <View style={styles.switchRow}>
            <View style={styles.switchTextWrap}>
              <Text style={styles.switchTitle}>24-Hour Live-in Care</Text>
              <Text style={styles.switchSubtitle}>Full-time residential caregiver or house manager</Text>
            </View>
            <Switch
              value={shifts.liveIn}
              onValueChange={(val) => setShifts({ ...shifts, liveIn: val })}
              trackColor={{ false: '#CBD5E1', true: '#94E0D1' }}
              thumbColor={shifts.liveIn ? '#0D7A68' : '#F1F5F9'}
            />
          </View>

          <View style={styles.switchRow}>
            <View style={styles.switchTextWrap}>
              <Text style={styles.switchTitle}>Emergency Urgent Callout</Text>
              <Text style={styles.switchSubtitle}>Immediate same-day dispatch for critical home support</Text>
            </View>
            <Switch
              value={shifts.emergencyCalls}
              onValueChange={(val) => setShifts({ ...shifts, emergencyCalls: val })}
              trackColor={{ false: '#CBD5E1', true: '#94E0D1' }}
              thumbColor={shifts.emergencyCalls ? '#0D7A68' : '#F1F5F9'}
            />
          </View>
        </View>

        {/* Operating Locations */}
        <View style={styles.card}>
          <Text style={styles.sectionTitle}>Operating Locations in Kenya</Text>
          <Text style={styles.sectionSubtitle}>Areas where you can travel without lodging issues</Text>
          <View style={styles.chipsWrap}>
            {locationsList.map((loc) => {
              const isSelected = selectedLocations.includes(loc);
              return (
                <TouchableOpacity
                  key={loc}
                  style={[styles.locationChip, isSelected && styles.locationChipActive]}
                  onPress={() => toggleLocation(loc)}
                >
                  <Ionicons
                    name={isSelected ? 'checkmark-circle' : 'location-outline'}
                    size={16}
                    color={isSelected ? '#0D7A68' : '#64748B'}
                  />
                  <Text style={[styles.locChipText, isSelected && styles.locChipTextActive]}>
                    {loc}
                  </Text>
                </TouchableOpacity>
              );
            })}
          </View>
        </View>

        {/* Save Button */}
        <TouchableOpacity style={styles.saveButton} onPress={handleSave}>
          <Ionicons name="save-outline" size={18} color="#FFFFFF" style={{ marginRight: 8 }} />
          <Text style={styles.saveButtonText}>Save Availability</Text>
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
  card: {
    backgroundColor: '#FFFFFF',
    marginHorizontal: 16,
    marginTop: 16,
    padding: 16,
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  sectionTitle: {
    fontSize: 15,
    fontWeight: '700',
    color: '#0F172A',
  },
  sectionSubtitle: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 2,
    marginBottom: 12,
  },
  daysRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  dayChip: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: '#F1F5F9',
    justifyContent: 'center',
    alignItems: 'center',
  },
  dayChipActive: {
    backgroundColor: '#0D7A68',
  },
  dayText: {
    fontSize: 12,
    fontWeight: '700',
    color: '#64748B',
  },
  dayTextActive: {
    color: '#FFFFFF',
  },
  switchRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#F1F5F9',
  },
  switchTextWrap: {
    flex: 1,
    marginRight: 12,
  },
  switchTitle: {
    fontSize: 14,
    fontWeight: '600',
    color: '#0F172A',
  },
  switchSubtitle: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 2,
  },
  chipsWrap: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  locationChip: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#F1F5F9',
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 20,
    gap: 6,
  },
  locationChipActive: {
    backgroundColor: '#E6F4F1',
    borderWidth: 1,
    borderColor: '#0D7A68',
  },
  locChipText: {
    fontSize: 13,
    color: '#475569',
    fontWeight: '500',
  },
  locChipTextActive: {
    color: '#0D7A68',
    fontWeight: '700',
  },
  saveButton: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#0D7A68',
    marginHorizontal: 16,
    marginTop: 24,
    paddingVertical: 14,
    borderRadius: 12,
  },
  saveButtonText: {
    fontSize: 15,
    fontWeight: '700',
    color: '#FFFFFF',
  },
});
