export type UserRole = 'CUSTOMER' | 'SPECIALIST';

export type ServiceCategory =
  | 'Pre & Post Pregnancy Care'
  | 'Post Surgery Care'
  | 'Elderly Care'
  | 'Physiotherapy'
  | 'Nanny & Housekeeping'
  | 'Special Needs Care'
  | 'Palliative Care';

export type ProfessionalCategory =
  | 'House Manager / Nanny'
  | 'Nurse'
  | 'Physiotherapist'
  | 'Nurse Aide or Assistant'
  | 'Special Needs Caregiver'
  | 'Home Health Assistant';

export interface Specialist {
  id: string;
  name: string;
  title: string;
  professionalCategory: ProfessionalCategory;
  serviceCategories: ServiceCategory[];
  bio: string;
  experienceYears: number;
  education: string;
  qualifications: string[];
  skills: string[];
  languages: string[];
  locations: string[];
  hourlyRateKsh: number;
  dailyRateKsh: number;
  monthlyRateKsh: number;
  verificationStatus: 'VERIFIED' | 'UNDER_REVIEW';
  verificationBadges: string[];
  rating: number;
  reviewCount: number;
  agencyAffiliation?: string;
  availableDays: string[];
  shiftTypes: string[];
  phoneNumber: string;
  email: string;
  isAvailable: boolean;
  isFavorite?: boolean;
}

export type BookingStatus = 'PENDING' | 'CONFIRMED' | 'COMPLETED' | 'CANCELLED';

export interface Booking {
  id: string;
  customerId: string;
  customerName: string;
  customerPhone: string;
  specialistId: string;
  specialistName: string;
  specialistTitle: string;
  serviceName: ServiceCategory;
  location: string;
  startDate: string;
  shiftType: string;
  durationDays: number;
  careRequirements: string;
  quoteKsh: number;
  status: BookingStatus;
  paymentStatus: 'UNPAID' | 'PAID' | 'REFUNDED';
  paymentMethod: string;
  createdAt: string;
}

export interface Review {
  id: string;
  specialistId: string;
  customerName: string;
  rating: number;
  comment: string;
  date: string;
  serviceName: string;
}

export interface SpecialistEarnings {
  totalGrossKsh: number;
  commissionRatePct: number; // Exactly 15.0%
  totalCommissionKsh: number;
  netEarningsKsh: number;
  pendingPayoutKsh: number;
  completedEngagements: number;
}

export interface SubscriptionPlan {
  id: string;
  name: string;
  priceKsh: number; // 500 KSh
  billingCycle: string;
  description: string;
  benefits: string[];
  isActive: boolean;
  renewalDate: string;
}
