import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation } from '@react-navigation/native';
import { INITIAL_SPECIALISTS } from '../../services/mockData';
import { Specialist } from '../../types';

export const FavoritesScreen: React.FC = () => {
  const navigation = useNavigation<any>();
  const [favorites, setFavorites] = useState<Specialist[]>([
    INITIAL_SPECIALISTS[0],
    INITIAL_SPECIALISTS[1],
  ]);

  const handleRemoveFavorite = (id: string) => {
    setFavorites(favorites.filter((s) => s.id !== id));
  };

  const renderFavoriteItem = ({ item }: { item: Specialist }) => (
    <TouchableOpacity
      style={styles.card}
      activeOpacity={0.8}
      onPress={() => navigation.navigate('SpecialistDetail', { specialistId: item.id })}
    >
      <View style={styles.avatarPlaceholder}>
        <Text style={styles.avatarLetter}>
          {item.name
            .split(' ')
            .map((n) => n[0])
            .slice(0, 2)
            .join('')}
        </Text>
      </View>
      <View style={styles.cardInfo}>
        <View style={styles.cardTop}>
          <Text style={styles.name}>{item.name}</Text>
          <TouchableOpacity onPress={() => handleRemoveFavorite(item.id)}>
            <Ionicons name="heart" size={20} color="#EF4444" />
          </TouchableOpacity>
        </View>
        <Text style={styles.category}>{item.title}</Text>
        <View style={styles.metaRow}>
          <Ionicons name="star" size={14} color="#F59E0B" />
          <Text style={styles.ratingText}>
            {item.rating} ({item.reviewCount})
          </Text>
          <Text style={styles.divider}>•</Text>
          <Text style={styles.locationText}>{item.locations[0]}</Text>
        </View>
        <View style={styles.priceRow}>
          <Text style={styles.price}>KSh {item.dailyRateKsh.toLocaleString()} / day</Text>
          <TouchableOpacity
            style={styles.bookButton}
            onPress={() => navigation.navigate('SpecialistDetail', { specialistId: item.id })}
          >
            <Text style={styles.bookButtonText}>Book</Text>
          </TouchableOpacity>
        </View>
      </View>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Saved Specialists</Text>
        <Text style={styles.subtitle}>Caregivers and nurses you have shortlisted</Text>
      </View>

      {favorites.length === 0 ? (
        <View style={styles.emptyContainer}>
          <Ionicons name="heart-dislike-outline" size={64} color="#CBD5E1" />
          <Text style={styles.emptyTitle}>No Favorites Yet</Text>
          <Text style={styles.emptySubtitle}>
            Browse vetted specialists across Kenya and tap the heart icon to save them here.
          </Text>
          <TouchableOpacity
            style={styles.exploreButton}
            onPress={() => navigation.navigate('CustomerExploreTab')}
          >
            <Text style={styles.exploreButtonText}>Browse Specialists</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <FlatList
          data={favorites}
          keyExtractor={(item) => item.id}
          renderItem={renderFavoriteItem}
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
  listContent: {
    padding: 16,
    gap: 12,
  },
  card: {
    flexDirection: 'row',
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    padding: 14,
    borderWidth: 1,
    borderColor: '#E2E8F0',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.05,
    shadowRadius: 3,
    elevation: 2,
  },
  avatarPlaceholder: {
    width: 72,
    height: 72,
    borderRadius: 12,
    backgroundColor: '#FBE8F0',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: '#400022',
  },
  avatarLetter: {
    fontSize: 20,
    fontWeight: '800',
    color: '#400022',
  },
  cardInfo: {
    flex: 1,
    marginLeft: 12,
    justifyContent: 'space-between',
  },
  cardTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  name: {
    fontSize: 15,
    fontWeight: '700',
    color: '#0F172A',
  },
  category: {
    fontSize: 12,
    color: '#400022',
    fontWeight: '600',
    marginTop: 2,
  },
  metaRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 4,
  },
  ratingText: {
    fontSize: 12,
    color: '#334155',
    marginLeft: 4,
    fontWeight: '500',
  },
  divider: {
    color: '#94A3B8',
    marginHorizontal: 6,
  },
  locationText: {
    fontSize: 12,
    color: '#64748B',
  },
  priceRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 8,
  },
  price: {
    fontSize: 13,
    fontWeight: '700',
    color: '#400022',
  },
  bookButton: {
    backgroundColor: '#400022',
    paddingHorizontal: 14,
    paddingVertical: 6,
    borderRadius: 8,
  },
  bookButtonText: {
    color: '#FFFFFF',
    fontSize: 12,
    fontWeight: '700',
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
  exploreButton: {
    marginTop: 20,
    backgroundColor: '#400022',
    paddingHorizontal: 20,
    paddingVertical: 12,
    borderRadius: 10,
  },
  exploreButtonText: {
    color: '#FFFFFF',
    fontWeight: '700',
    fontSize: 14,
  },
});
