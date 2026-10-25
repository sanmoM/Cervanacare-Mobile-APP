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
import { useAuth } from '../../context/AuthContext';

export const SpecialistEarningsScreen: React.FC = () => {
  const { user } = useAuth();

  const [availableBalance, setAvailableBalance] = useState<number>(38250);
  const [totalEarned, setTotalEarned] = useState<number>(45000);
  const [commissionPaid, setCommissionPaid] = useState<number>(6750);
  const [withdrawModalVisible, setWithdrawModalVisible] = useState(false);
  const [withdrawPhone, setWithdrawPhone] = useState('+254 712 345 678');
  const [withdrawAmount, setWithdrawAmount] = useState('38250');
  const [isProcessing, setIsProcessing] = useState(false);

  const [transactions, setTransactions] = useState([
    {
      id: 'tx-1',
      date: '24 Oct 2026',
      description: 'Post Surgery Care - Mrs. Kamau',
      gross: 4500,
      fee: 675,
      net: 3825,
      type: 'PAYOUT',
    },
    {
      id: 'tx-2',
      date: '21 Oct 2026',
      description: 'Elderly Home Care - Dr. Otieno',
      gross: 9000,
      fee: 1350,
      net: 7650,
      type: 'PAYOUT',
    },
    {
      id: 'tx-3',
      date: '18 Oct 2026',
      description: 'M-Pesa B2C Payout to 0712***678',
      gross: 0,
      fee: 0,
      net: -25000,
      type: 'WITHDRAWAL',
    },
  ]);

  const handleWithdraw = () => {
    const num = parseInt(withdrawAmount, 10);
    if (isNaN(num) || num <= 0 || num > availableBalance) {
      Alert.alert('Invalid Amount', `Please enter a valid amount up to KSh ${availableBalance.toLocaleString()}`);
      return;
    }

    setIsProcessing(true);
    setTimeout(() => {
      setIsProcessing(false);
      setWithdrawModalVisible(false);
      const newBal = availableBalance - num;
      setAvailableBalance(newBal);

      const refCode = 'QK' + Math.floor(10000000 + Math.random() * 90000000);
      setTransactions([
        {
          id: `tx-${Date.now()}`,
          date: 'Just now',
          description: `M-Pesa B2C Payout (${refCode})`,
          gross: 0,
          fee: 0,
          net: -num,
          type: 'WITHDRAWAL',
        },
        ...transactions,
      ]);

      Alert.alert(
        'M-Pesa Payout Successful',
        `${refCode} Confirmed. KSh ${num.toLocaleString()} sent to ${withdrawPhone}. Transaction fees: KSh 0.`
      );
    }, 1500);
  };

  return (
    <View style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scrollContent}>
        <View style={styles.header}>
          <Text style={styles.title}>Earnings & Payouts</Text>
          <Text style={styles.subtitle}>Direct Safaricom Lipa Na M-Pesa disbursements</Text>
        </View>

        {/* Balance Card */}
        <View style={styles.balanceCard}>
          <Text style={styles.balanceLabel}>Available for Instant M-Pesa Withdrawal</Text>
          <Text style={styles.balanceAmount}>KSh {availableBalance.toLocaleString()}</Text>

          <TouchableOpacity
            style={styles.withdrawButton}
            onPress={() => setWithdrawModalVisible(true)}
            disabled={availableBalance <= 0}
          >
            <Ionicons name="phone-portrait-outline" size={18} color="#FFFFFF" style={{ marginRight: 6 }} />
            <Text style={styles.withdrawButtonText}>Withdraw via M-Pesa B2C</Text>
          </TouchableOpacity>
        </View>

        {/* Healthcare vs Domestic Subscription Transparency */}
        <View style={styles.commissionCard}>
          <View style={styles.commHeader}>
            <Ionicons name="shield-checkmark" size={18} color="#0D7A68" />
            <Text style={styles.commTitle}>Cervanna Fair Pricing Model</Text>
          </View>
          <Text style={styles.commDesc}>
            • Healthcare Services (Nurses, Physiotherapists): 15% platform commission deducted on completed visits.
          </Text>
          <Text style={styles.commDesc}>
            • Domestic Workers (House Managers, Nannies): KSh 500/month flat subscription with zero commission on client salary.
          </Text>

          <View style={styles.statBreakdownRow}>
            <View style={styles.statBox}>
              <Text style={styles.statLabel}>Total Billed</Text>
              <Text style={styles.statValue}>KSh {totalEarned.toLocaleString()}</Text>
            </View>
            <View style={styles.statBox}>
              <Text style={styles.statLabel}>Platform Fees (15%)</Text>
              <Text style={styles.statValue}>KSh {commissionPaid.toLocaleString()}</Text>
            </View>
          </View>
        </View>

        {/* Domestic Worker Subscription Banner */}
        <View style={styles.subscriptionCard}>
          <View style={styles.subTop}>
            <Text style={styles.subTitle}>Domestic Specialist Subscription</Text>
            <View style={styles.activePill}>
              <Text style={styles.activeText}>ACTIVE</Text>
            </View>
          </View>
          <Text style={styles.subDetails}>
            Tier: Individual Professional (KSh 500 / month) • Next renewal: 14 Nov 2026
          </Text>
        </View>

        {/* Transaction History */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Transaction Ledger</Text>
          {transactions.map((tx) => (
            <View key={tx.id} style={styles.txCard}>
              <View style={styles.txIconWrap}>
                <Ionicons
                  name={tx.type === 'WITHDRAWAL' ? 'arrow-up-circle' : 'arrow-down-circle'}
                  size={24}
                  color={tx.type === 'WITHDRAWAL' ? '#EF4444' : '#0D7A68'}
                />
              </View>
              <View style={styles.txInfo}>
                <Text style={styles.txDesc}>{tx.description}</Text>
                <Text style={styles.txDate}>{tx.date}</Text>
                {tx.gross > 0 ? (
                  <Text style={styles.txFee}>
                    Gross: KSh {tx.gross.toLocaleString()} (15% Fee: -KSh {tx.fee.toLocaleString()})
                  </Text>
                ) : null}
              </View>
              <Text
                style={[
                  styles.txAmount,
                  tx.type === 'WITHDRAWAL' ? styles.txWithdrawal : styles.txPayout,
                ]}
              >
                {tx.net > 0 ? `+KSh ${tx.net.toLocaleString()}` : `-KSh ${Math.abs(tx.net).toLocaleString()}`}
              </Text>
            </View>
          ))}
        </View>
      </ScrollView>

      {/* M-Pesa B2C Withdrawal Modal */}
      <Modal visible={withdrawModalVisible} animationType="slide" transparent>
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <View style={styles.modalHeader}>
              <Text style={styles.modalTitle}>Withdraw to M-Pesa</Text>
              <TouchableOpacity onPress={() => setWithdrawModalVisible(false)}>
                <Ionicons name="close" size={24} color="#64748B" />
              </TouchableOpacity>
            </View>

            <Text style={styles.inputLabel}>Safaricom M-Pesa Number</Text>
            <TextInput
              style={styles.input}
              value={withdrawPhone}
              onChangeText={setWithdrawPhone}
              keyboardType="phone-pad"
            />

            <Text style={styles.inputLabel}>Withdrawal Amount (KSh)</Text>
            <TextInput
              style={styles.input}
              value={withdrawAmount}
              onChangeText={setWithdrawAmount}
              keyboardType="numeric"
            />
            <Text style={styles.maxText}>Maximum available: KSh {availableBalance.toLocaleString()}</Text>

            <TouchableOpacity
              style={styles.confirmWithdrawBtn}
              onPress={handleWithdraw}
              disabled={isProcessing}
            >
              <Text style={styles.confirmWithdrawText}>
                {isProcessing ? 'Processing Disbursement...' : 'Disburse to M-Pesa Instantly'}
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
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
  balanceCard: {
    backgroundColor: '#0D7A68',
    marginHorizontal: 16,
    marginTop: 16,
    padding: 20,
    borderRadius: 16,
  },
  balanceLabel: {
    fontSize: 13,
    color: '#E6F4F1',
    fontWeight: '500',
  },
  balanceAmount: {
    fontSize: 28,
    fontWeight: '800',
    color: '#FFFFFF',
    marginTop: 6,
  },
  withdrawButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#059669',
    marginTop: 16,
    paddingVertical: 12,
    borderRadius: 10,
  },
  withdrawButtonText: {
    color: '#FFFFFF',
    fontWeight: '700',
    fontSize: 14,
  },
  commissionCard: {
    backgroundColor: '#FFFFFF',
    marginHorizontal: 16,
    marginTop: 16,
    padding: 16,
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  commHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  commTitle: {
    fontSize: 14,
    fontWeight: '700',
    color: '#0D7A68',
    marginLeft: 6,
  },
  commDesc: {
    fontSize: 12,
    color: '#475569',
    marginTop: 4,
    lineHeight: 18,
  },
  statBreakdownRow: {
    flexDirection: 'row',
    marginTop: 14,
    gap: 10,
  },
  statBox: {
    flex: 1,
    backgroundColor: '#F8FAFC',
    padding: 10,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  statLabel: {
    fontSize: 11,
    color: '#64748B',
  },
  statValue: {
    fontSize: 14,
    fontWeight: '700',
    color: '#0F172A',
    marginTop: 2,
  },
  subscriptionCard: {
    backgroundColor: '#FFFFFF',
    marginHorizontal: 16,
    marginTop: 12,
    padding: 16,
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  subTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  subTitle: {
    fontSize: 14,
    fontWeight: '700',
    color: '#0F172A',
  },
  activePill: {
    backgroundColor: '#ECFDF5',
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
  },
  activeText: {
    color: '#065F46',
    fontSize: 11,
    fontWeight: '700',
  },
  subDetails: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 6,
  },
  section: {
    marginTop: 16,
    paddingHorizontal: 16,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#0F172A',
    marginBottom: 10,
  },
  txCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    padding: 14,
    borderRadius: 12,
    marginBottom: 8,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  txIconWrap: {
    marginRight: 12,
  },
  txInfo: {
    flex: 1,
  },
  txDesc: {
    fontSize: 14,
    fontWeight: '600',
    color: '#0F172A',
  },
  txDate: {
    fontSize: 11,
    color: '#94A3B8',
    marginTop: 2,
  },
  txFee: {
    fontSize: 11,
    color: '#64748B',
    marginTop: 2,
  },
  txAmount: {
    fontSize: 14,
    fontWeight: '700',
  },
  txPayout: {
    color: '#0D7A68',
  },
  txWithdrawal: {
    color: '#EF4444',
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
  input: {
    backgroundColor: '#F8FAFC',
    borderWidth: 1,
    borderColor: '#E2E8F0',
    borderRadius: 8,
    padding: 12,
    fontSize: 15,
    color: '#0F172A',
  },
  maxText: {
    fontSize: 12,
    color: '#64748B',
    marginTop: 4,
  },
  confirmWithdrawBtn: {
    backgroundColor: '#0D7A68',
    paddingVertical: 14,
    borderRadius: 12,
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 10,
  },
  confirmWithdrawText: {
    color: '#FFFFFF',
    fontSize: 15,
    fontWeight: '700',
  },
});
