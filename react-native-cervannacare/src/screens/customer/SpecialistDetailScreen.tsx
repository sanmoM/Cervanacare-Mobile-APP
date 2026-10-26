import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  TextInput,
  Alert,
  Modal,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRoute, useNavigation } from '@react-navigation/native';
import { INITIAL_SPECIALISTS } from '../../services/mockData';
import { MpesaModal } from '../../components/MpesaModal';

export const SpecialistDetailScreen: React.FC = () => {
  const route = useRoute<any>();
  const navigation = useNavigation<any>();
  const { specialistId } = route.params || {};

  const specialist = INITIAL_SPECIALISTS.find((s) => s.id === specialistId) || INITIAL_SPECIALISTS[0];

  const [isFavorite, setIsFavorite] = useState(false);
  const [selectedDate, setSelectedDate] = useState('Tomorrow, 8:00 AM');
  const [careNotes, setCareNotes] = useState('');
  const [bookingModalVisible, setBookingModalVisible] = useState(false);
  const [mpesaVisible, setMpesaVisible] = useState(false);

  const isHealth = specialist.professionalCategory !== 'House Manager / Nanny';
  const platformFee = isHealth ? Math.round(specialist.dailyRateKsh * 0.15) : 0;
  const totalAmount = specialist.dailyRateKsh;

  const handleBookNow = () => {
    setBookingModalVisible(true);
  };

  const handleConfirmBookingDetails = () => {
    setBookingModalVisible(false);
    setMpesaVisible(true);
  };

  const handleMpesaSuccess = (receipt: string) => {
    setMpesaVisible(false);
    Alert.alert(
      'Booking Confirmed!',
      `Your booking request for ${specialist.name} has been submitted with M-Pesa receipt ${receipt}. The specialist will confirm shortly.`,
      [{ text: 'View Bookings', onPress: () => navigation.navigate('CustomerBookingsTab') }]
    );
  };

  return (
    <View style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scrollContent}>
        {/* Header Bar */}
        <View style={styles.navBar}>
          <TouchableOpacity onPress={() => navigation.goBack()} style={styles.navButton}>
            <Ionicons name="arrow-back" size={24} color="#0F172A" />
          </TouchableOpacity>
          <Text style={styles.navTitle} numberOfLines={1}>
            {specialist.name}
          </Text>
          <TouchableOpacity onPress={() => setIsFavorite(!isFavorite)} style={styles.navButton}>
            <Ionicons
              name={isFavorite ? 'heart' : 'heart-outline'}
              size={24}
              color={isFavorite ? '#EF4444' : '#0F172A'}
            />
          </TouchableOpacity>
        </View>

        {/* Hero Card */}
        <View style={styles.heroCard}>
          <View style={styles.avatarPlaceholder}>
            <Text style={styles.avatarLetter}>
              {specialist.name
                .split(' ')
                .map((n) => n[0])
                .slice(0, 2)
                .join('')}
            </Text>
          </View>
          <View style={styles.heroInfo}>
            <View style={styles.verificationBadge}>
              <Ionicons name="shield-checkmark" size={14} color="#400022" />
              <Text style={styles.verificationText}>Vetted & Background Checked</Text>
            </View>
            <Text style={styles.name}>{specialist.name}</Text>
            <Text style={styles.category}>{specialist.title}</Text>
            <View style={styles.ratingRow}>
              <Ionicons name="star" size={16} color="#F59E0B" />
              <Text style={styles.ratingText}>
                {specialist.rating} ({specialist.reviewCount} verified reviews)
              </Text>
            </View>
            <View style={styles.locationRow}>
              <Ionicons name="location-outline" size={16} color="#64748B" />
              <Text style={styles.locationText}>{specialist.locations.join(', ')}</Text>
            </View>
          </View>
        </View>

        {/* Rate Banner */}
        <View style={styles.rateCard}>
          <View>
            <Text style={styles.rateLabel}>Daily Care Rate</Text>
            <Text style={styles.rateValue}>KSh {specialist.dailyRateKsh.toLocaleString()} / day</Text>
            {specialist.monthlyRateKsh ? (
              <Text style={styles.subRate}>KSh {specialist.monthlyRateKsh.toLocaleString()} / month</Text>
            ) : null}
          </View>
          <View style={styles.availabilityPill}>
            <View style={styles.onlineDot} />
            <Text style={styles.availabilityText}>
              {specialist.isAvailable ? 'Available Now' : 'Booked'}
            </Text>
          </View>
        </View>

        {/* Bio Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>About Specialist</Text>
          <Text style={styles.bioText}>{specialist.bio}</Text>
        </View>

        {/* Verification Credentials */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Credentials & Certifications</Text>
          <View style={styles.credentialItem}>
            <Ionicons name="ribbon-outline" size={20} color="#400022" />
            <View style={styles.credentialTextWrap}>
              <Text style={styles.credentialTitle}>Education & Experience</Text>
              <Text style={styles.credentialDesc}>
                {specialist.education} • {specialist.experienceYears} years active clinical experience
              </Text>
            </View>
          </View>
          {specialist.qualifications.map((q, idx) => (
            <View key={idx} style={styles.credentialItem}>
              <Ionicons name="checkmark-circle-outline" size={20} color="#400022" />
              <View style={styles.credentialTextWrap}>
                <Text style={styles.credentialTitle}>{q}</Text>
              </View>
            </View>
          ))}
        </View>

        {/* Skills */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Skills & Expertise</Text>
          <View style={styles.chipsWrap}>
            {specialist.skills.map((skill, index) => (
              <View key={index} style={styles.chip}>
                <Text style={styles.chipText}>{skill}</Text>
              </View>
            ))}
          </View>
        </View>

        {/* Languages */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Languages</Text>
          <Text style={styles.bodyText}>{specialist.languages.join(' • ')}</Text>
        </View>
      </ScrollView>

      {/* Bottom Action Footer */}
      <View style={styles.bottomBar}>
        <View style={styles.priceContainer}>
          <Text style={styles.bottomPriceLabel}>Starting from</Text>
          <Text style={styles.bottomPriceValue}>KSh {specialist.dailyRateKsh.toLocaleString()}</Text>
        </View>
        <TouchableOpacity style={styles.bookButton} onPress={handleBookNow}>
          <Ionicons name="calendar-outline" size={18} color="#FFFFFF" style={{ marginRight: 6 }} />
          <Text style={styles.bookButtonText}>Book Caregiver</Text>
        </TouchableOpacity>
      </View>

      {/* Booking Form Sheet */}
      <Modal visible={bookingModalVisible} animationType="slide" transparent>
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <View style={styles.modalHeader}>
              <Text style={styles.modalTitle}>Request Care Service</Text>
              <TouchableOpacity onPress={() => setBookingModalVisible(false)}>
                <Ionicons name="close" size={24} color="#64748B" />
              </TouchableOpacity>
            </View>

            <ScrollView showsVerticalScrollIndicator={false}>
              <Text style={styles.inputLabel}>Service Details</Text>
              <View style={styles.readonlyBox}>
                <Text style={styles.readonlyText}>
                  {specialist.professionalCategory} with {specialist.name}
                </Text>
              </View>

              <Text style={styles.inputLabel}>Select Date & Shift</Text>
              <TextInput
                style={styles.input}
                value={selectedDate}
                onChangeText={setSelectedDate}
                placeholder="e.g. 28 Oct 2026, 8:00 AM - 5:00 PM"
                placeholderTextColor="#ddd"
              />

              <Text style={styles.inputLabel}>Patient Care Notes / Requirements</Text>
              <TextInput
                style={[styles.input, styles.textArea]}
                value={careNotes}
                onChangeText={setCareNotes}
                placeholder="Describe patient condition, mobility needs, medications, or any special requests..."
                placeholderTextColor="#ddd"
                multiline
                numberOfLines={4}
              />

              {/* Price Breakdown */}
              <View style={styles.breakdownCard}>
                <Text style={styles.breakdownTitle}>Price Breakdown (Kenya Shillings)</Text>
                <View style={styles.breakdownRow}>
                  <Text style={styles.breakdownLabel}>Service Rate (1 Day):</Text>
                  <Text style={styles.breakdownValue}>KSh {specialist.dailyRateKsh.toLocaleString()}</Text>
                </View>
                {isHealth ? (
                  <View style={styles.breakdownRow}>
                    <Text style={styles.breakdownSub}>Includes 15% Cervanna platform fee:</Text>
                    <Text style={styles.breakdownSub}>KSh {platformFee.toLocaleString()}</Text>
                  </View>
                ) : (
                  <View style={styles.breakdownRow}>
                    <Text style={styles.breakdownSub}>Domestic worker flat placement:</Text>
                    <Text style={styles.breakdownSub}>No customer commission</Text>
                  </View>
                )}
                <View style={[styles.breakdownRow, styles.totalRow]}>
                  <Text style={styles.totalLabel}>Total Payable:</Text>
                  <Text style={styles.totalValue}>KSh {totalAmount.toLocaleString()}</Text>
                </View>
              </View>

              <TouchableOpacity style={styles.proceedButton} onPress={handleConfirmBookingDetails}>
                <Text style={styles.proceedButtonText}>Proceed to M-Pesa Payment</Text>
              </TouchableOpacity>
            </ScrollView>
          </View>
        </View>
      </Modal>

      {/* Lipa Na M-Pesa STK Push Modal */}
      <MpesaModal
        visible={mpesaVisible}
        onClose={() => setMpesaVisible(false)}
        amount={totalAmount}
        serviceDescription={`Care Booking: ${specialist.name}`}
        onPaymentSuccess={handleMpesaSuccess}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F8FAFC',
  },
  scrollContent: {
    paddingBottom: 100,
  },
  navBar: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingTop: 50,
    paddingBottom: 12,
    backgroundColor: '#FFFFFF',
    borderBottomWidth: 1,
    borderBottomColor: '#E2E8F0',
  },
  navButton: {
    padding: 8,
  },
  navTitle: {
    fontSize: 17,
    fontWeight: '700',
    color: '#0F172A',
    flex: 1,
    textAlign: 'center',
    marginHorizontal: 8,
  },
  heroCard: {
    flexDirection: 'row',
    backgroundColor: '#FFFFFF',
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#E2E8F0',
  },
  avatarPlaceholder: {
    width: 90,
    height: 90,
    borderRadius: 16,
    backgroundColor: '#FBE8F0',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: '#400022',
  },
  avatarLetter: {
    fontSize: 26,
    fontWeight: '800',
    color: '#400022',
  },
  heroInfo: {
    flex: 1,
    marginLeft: 14,
    justifyContent: 'center',
  },
  verificationBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#FBE8F0',
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
    alignSelf: 'flex-start',
    marginBottom: 6,
  },
  verificationText: {
    fontSize: 11,
    fontWeight: '600',
    color: '#400022',
    marginLeft: 4,
  },
  name: {
    fontSize: 18,
    fontWeight: '700',
    color: '#0F172A',
  },
  category: {
    fontSize: 13,
    fontWeight: '600',
    color: '#400022',
    marginTop: 2,
  },
  ratingRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 6,
  },
  ratingText: {
    fontSize: 13,
    color: '#334155',
    fontWeight: '500',
    marginLeft: 4,
  },
  locationRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 4,
  },
  locationText: {
    fontSize: 12,
    color: '#64748B',
    marginLeft: 4,
  },
  rateCard: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    marginHorizontal: 16,
    marginTop: 14,
    padding: 16,
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  rateLabel: {
    fontSize: 12,
    color: '#64748B',
    fontWeight: '500',
  },
  rateValue: {
    fontSize: 18,
    fontWeight: '700',
    color: '#400022',
    marginTop: 2,
  },
  subRate: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 2,
  },
  availabilityPill: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#ECFDF5',
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 20,
  },
  onlineDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: '#10B981',
    marginRight: 6,
  },
  availabilityText: {
    fontSize: 12,
    fontWeight: '600',
    color: '#065F46',
  },
  section: {
    backgroundColor: '#FFFFFF',
    marginTop: 14,
    padding: 16,
    borderTopWidth: 1,
    borderBottomWidth: 1,
    borderColor: '#E2E8F0',
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#0F172A',
    marginBottom: 8,
  },
  bioText: {
    fontSize: 14,
    color: '#334155',
    lineHeight: 22,
  },
  credentialItem: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    marginTop: 10,
  },
  credentialTextWrap: {
    marginLeft: 12,
    flex: 1,
  },
  credentialTitle: {
    fontSize: 13,
    fontWeight: '600',
    color: '#0F172A',
  },
  credentialDesc: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 2,
  },
  chipsWrap: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  chip: {
    backgroundColor: '#FBE8F0',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
  },
  chipText: {
    fontSize: 13,
    color: '#400022',
    fontWeight: '500',
  },
  bodyText: {
    fontSize: 14,
    color: '#334155',
  },
  bottomBar: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 20,
    paddingVertical: 14,
    borderTopWidth: 1,
    borderTopColor: '#E2E8F0',
  },
  priceContainer: {
    justifyContent: 'center',
  },
  bottomPriceLabel: {
    fontSize: 11,
    color: '#64748B',
  },
  bottomPriceValue: {
    fontSize: 18,
    fontWeight: '700',
    color: '#400022',
  },
  bookButton: {
    flexDirection: 'row',
    backgroundColor: '#400022',
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: 12,
    alignItems: 'center',
  },
  bookButtonText: {
    color: '#FFFFFF',
    fontWeight: '700',
    fontSize: 15,
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'flex-end',
  },
  modalContent: {
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    padding: 20,
    maxHeight: '80%',
  },
  modalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: '700',
    color: '#0F172A',
  },
  inputLabel: {
    fontSize: 13,
    fontWeight: '600',
    color: '#334155',
    marginTop: 12,
    marginBottom: 6,
  },
  readonlyBox: {
    backgroundColor: '#F1F5F9',
    padding: 12,
    borderRadius: 8,
  },
  readonlyText: {
    fontSize: 14,
    color: '#0F172A',
    fontWeight: '600',
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
  breakdownCard: {
    backgroundColor: '#F8FAFC',
    padding: 14,
    borderRadius: 10,
    marginTop: 16,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  breakdownTitle: {
    fontSize: 13,
    fontWeight: '700',
    color: '#0F172A',
  },
  breakdownRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginVertical: 3,
  },
  breakdownLabel: {
    fontSize: 13,
    color: '#334155',
  },
  breakdownValue: {
    fontSize: 13,
    fontWeight: '600',
    color: '#0F172A',
  },
  breakdownSub: {
    fontSize: 12,
    color: '#64748B',
  },
  totalRow: {
    borderTopWidth: 1,
    borderTopColor: '#E2E8F0',
    paddingTop: 8,
    marginTop: 8,
  },
  totalLabel: {
    fontSize: 14,
    fontWeight: '700',
    color: '#0F172A',
  },
  totalValue: {
    fontSize: 16,
    fontWeight: '800',
    color: '#400022',
  },
  proceedButton: {
    backgroundColor: '#400022',
    paddingVertical: 14,
    borderRadius: 12,
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 10,
  },
  proceedButtonText: {
    color: '#FFFFFF',
    fontWeight: '700',
    fontSize: 15,
  },
});
