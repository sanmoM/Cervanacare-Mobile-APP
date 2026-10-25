import React, { createContext, useContext, useState, useEffect } from 'react';
import * as SecureStore from 'expo-secure-store';
import { UserRole } from '../types';

interface UserSession {
  token: string | null;
  role: UserRole;
  email: string | null;
  name: string | null;
}

interface AuthContextType {
  isAuthenticated: boolean;
  isLoading: boolean;
  userRole: UserRole;
  userSession: UserSession;
  login: (role: UserRole, email: string, name: string) => Promise<void>;
  logout: () => Promise<void>;
  switchRole: (role: UserRole) => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const TOKEN_KEY = 'cervanna_sec_token';
const ROLE_KEY = 'cervanna_sec_role';
const EMAIL_KEY = 'cervanna_sec_email';
const NAME_KEY = 'cervanna_sec_name';

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [userRole, setUserRole] = useState<UserRole>('CUSTOMER');
  const [userSession, setUserSession] = useState<UserSession>({
    token: null,
    role: 'CUSTOMER',
    email: null,
    name: null,
  });

  useEffect(() => {
    loadStoredSession();
  }, []);

  const loadStoredSession = async () => {
    try {
      const token = await SecureStore.getItemAsync(TOKEN_KEY);
      const roleStr = await SecureStore.getItemAsync(ROLE_KEY);
      const email = await SecureStore.getItemAsync(EMAIL_KEY);
      const name = await SecureStore.getItemAsync(NAME_KEY);

      if (token) {
        const role = (roleStr === 'SPECIALIST' ? 'SPECIALIST' : 'CUSTOMER') as UserRole;
        setUserRole(role);
        setUserSession({
          token,
          role,
          email: email || 'user@cervannacare.com',
          name: name || (role === 'CUSTOMER' ? 'Sarah Kamau' : 'Faith Muthoni, RN'),
        });
        setIsAuthenticated(true);
      }
    } catch (e) {
      console.warn('Failed to load session from SecureStore', e);
    } finally {
      setIsLoading(false);
    }
  };

  const login = async (role: UserRole, email: string, name: string) => {
    const token = 'jwt_cervanna_' + Date.now();
    try {
      await SecureStore.setItemAsync(TOKEN_KEY, token);
      await SecureStore.setItemAsync(ROLE_KEY, role);
      await SecureStore.setItemAsync(EMAIL_KEY, email);
      await SecureStore.setItemAsync(NAME_KEY, name);

      setUserRole(role);
      setUserSession({ token, role, email, name });
      setIsAuthenticated(true);
    } catch (e) {
      console.error('Failed to save to SecureStore', e);
    }
  };

  const logout = async () => {
    try {
      await SecureStore.deleteItemAsync(TOKEN_KEY);
      await SecureStore.deleteItemAsync(ROLE_KEY);
      await SecureStore.deleteItemAsync(EMAIL_KEY);
      await SecureStore.deleteItemAsync(NAME_KEY);
    } catch (e) {
      console.warn('Failed to clear SecureStore', e);
    } finally {
      setIsAuthenticated(false);
      setUserSession({ token: null, role: 'CUSTOMER', email: null, name: null });
    }
  };

  const switchRole = async (newRole: UserRole) => {
    try {
      await SecureStore.setItemAsync(ROLE_KEY, newRole);
      setUserRole(newRole);
      setUserSession((prev) => ({ ...prev, role: newRole }));
    } catch (e) {
      console.error('Failed to update role in SecureStore', e);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        isLoading,
        userRole,
        userSession,
        login,
        logout,
        switchRole,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within an AuthProvider');
  return context;
};
