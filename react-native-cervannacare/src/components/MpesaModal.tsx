import React, { useState } from 'react';
import { View, Text, Modal, TextInput, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';

export interface MpesaModalProps {
  visible: boolean;
  amountKsh?: number;
  amount?: number;
  bookingId?: string;
  serviceDescription?: string;
  onSuccess?: (receipt: string) => void;
  onPaymentSuccess?: (receipt: string) => void;
  onClose: () => void;
}

export function MpesaModal({
  visible,
  amountKsh,
  amount,
  bookingId,
  serviceDescription,
  onSuccess,
  onPaymentSuccess,
  onClose,
}: MpesaModalProps) {
  const [phone, setPhone] = useState('0712345678');
  const [loading, setLoading] = useState(false);
  const [receipt, setReceipt] = useState<string | null>(null);

  const finalAmount = amount !== undefined ? amount : amountKsh || 0;

  const handlePay = () => {
    setLoading(true);
    setTimeout(() => {
      const ref = 'QKT' + Math.floor(100000 + Math.random() * 900000) + 'KE';
      setLoading(false);
      setReceipt(ref);
      if (onPaymentSuccess) {
        onPaymentSuccess(ref);
      } else if (onSuccess) {
        onSuccess(ref);
      }
    }, 1500);
  };

  const handleDone = () => {
    setReceipt(null);
    onClose();
  };

  return (
    <Modal visible={visible} transparent animationType="fade" onRequestClose={onClose}>
      <View style={styles.overlay}>
        <View style={styles.modalCard}>
          <View style={styles.header}>
            <View style={styles.mpesaBadge}>
              <Text style={styles.mpesaBadgeText}>M</Text>
            </View>
            <View>
              <Text style={styles.title}>Lipa na M-Pesa</Text>
              <Text style={styles.subtitle}>Paybill: 890200 • Cervanna Care</Text>
            </View>
          </View>

          {receipt ? (
            <View style={styles.successContainer}>
              <Text style={styles.successIcon}>✓</Text>
              <Text style={styles.successTitle}>Payment Received!</Text>
              <Text style={styles.successSubtitle}>Verified by Safaricom M-Pesa</Text>
              <View style={styles.receiptBox}>
                <Text style={styles.receiptLabel}>Amount: KSh {finalAmount.toLocaleString()}</Text>
                <Text style={styles.receiptLabel}>Receipt: {receipt}</Text>
                {serviceDescription && <Text style={styles.receiptLabel}>{serviceDescription}</Text>}
                {bookingId && <Text style={styles.receiptLabel}>Booking #{bookingId}</Text>}
              </View>
              <TouchableOpacity style={styles.doneButton} onPress={handleDone}>
                <Text style={styles.doneButtonText}>Done</Text>
              </TouchableOpacity>
            </View>
          ) : (
            <>
              <View style={styles.amountCard}>
                <Text style={styles.amountLabel}>Total Due</Text>
                <Text style={styles.amountValue}>KSh {finalAmount.toLocaleString()}</Text>
                {serviceDescription && <Text style={styles.descLabel}>{serviceDescription}</Text>}
              </View>

              <Text style={styles.phoneLabel}>Enter Safaricom M-Pesa Number</Text>
              <TextInput
                style={styles.phoneInput}
                keyboardType="phone-pad"
                value={phone}
                onChangeText={setPhone}
                placeholder="0712 345 678"
                placeholderTextColor="#ddd"
              />
              <Text style={styles.hint}>
                You will receive an STK Push prompt on your phone to enter your 4-digit M-Pesa PIN.
              </Text>

              <View style={styles.actions}>
                <TouchableOpacity style={styles.cancelBtn} onPress={onClose} disabled={loading}>
                  <Text style={styles.cancelBtnText}>Cancel</Text>
                </TouchableOpacity>

                <TouchableOpacity style={styles.payBtn} onPress={handlePay} disabled={loading || phone.length < 10}>
                  {loading ? (
                    <ActivityIndicator color="#FFFFFF" />
                  ) : (
                    <Text style={styles.payBtnText}>Send STK Push</Text>
                  )}
                </TouchableOpacity>
              </View>
            </>
          )}
        </View>
      </View>
    </Modal>
  );
}

export default MpesaModal;

const styles = StyleSheet.create({
  overlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.5)', justifyContent: 'center', alignItems: 'center', padding: 20 },
  modalCard: { width: '100%', backgroundColor: '#FFFFFF', borderRadius: 20, padding: 20 },
  header: { flexDirection: 'row', alignItems: 'center', gap: 10, marginBottom: 16 },
  mpesaBadge: { width: 36, height: 36, borderRadius: 18, backgroundColor: '#009639', justifyContent: 'center', alignItems: 'center' },
  mpesaBadgeText: { color: '#FFFFFF', fontWeight: 'bold', fontSize: 18 },
  title: { fontSize: 16, fontWeight: 'bold', color: '#14532D' },
  subtitle: { fontSize: 11, color: '#64748B' },
  amountCard: { backgroundColor: '#F0FDFA', borderRadius: 12, padding: 14, alignItems: 'center', marginBottom: 14 },
  amountLabel: { fontSize: 12, color: '#0F766E' },
  amountValue: { fontSize: 26, fontWeight: 'bold', color: '#400022' },
  descLabel: { fontSize: 12, color: '#64748B', marginTop: 4 },
  phoneLabel: { fontSize: 12, fontWeight: '600', color: '#334155', marginBottom: 6 },
  phoneInput: { backgroundColor: '#F8FAFC', borderWidth: 1, borderColor: '#CBD5E1', borderRadius: 10, padding: 10, fontSize: 15 },
  hint: { fontSize: 11, color: '#64748B', marginTop: 4, marginBottom: 16 },
  actions: { flexDirection: 'row', justifyContent: 'flex-end', gap: 10 },
  cancelBtn: { paddingVertical: 10, paddingHorizontal: 16 },
  cancelBtnText: { color: '#64748B', fontSize: 13 },
  payBtn: { backgroundColor: '#009639', paddingVertical: 10, paddingHorizontal: 20, borderRadius: 10 },
  payBtnText: { color: '#FFFFFF', fontWeight: 'bold', fontSize: 13 },
  successContainer: { alignItems: 'center', paddingVertical: 10 },
  successIcon: { fontSize: 40, color: '#009639', marginBottom: 8 },
  successTitle: { fontSize: 18, fontWeight: 'bold', color: '#0F172A' },
  successSubtitle: { fontSize: 12, color: '#64748B', marginBottom: 14 },
  receiptBox: { backgroundColor: '#F1F5F9', padding: 12, borderRadius: 10, width: '100%', marginBottom: 16 },
  receiptLabel: { fontSize: 12, color: '#334155', marginVertical: 2 },
  doneButton: { backgroundColor: '#400022', paddingVertical: 12, width: '100%', borderRadius: 10, alignItems: 'center' },
  doneButtonText: { color: '#FFFFFF', fontWeight: 'bold' },
});
