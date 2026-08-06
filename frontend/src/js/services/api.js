define([], function () {
  'use strict';
  const API_BASE = window.localStorage.getItem('securelife.apiBase') || 'http://localhost:8080';
  const TOKEN_KEY = 'securelife.token';
  const USER_KEY = 'securelife.user';
  const CONTEXT_KEY = 'securelife.context';

  function currentUser() {
    try { return JSON.parse(window.localStorage.getItem(USER_KEY)); }
    catch (e) { return null; }
  }
  function contextKey() {
    const user = currentUser();
    return CONTEXT_KEY + '.' + (user && user.id ? user.id : 'guest');
  }
  function notifyAuth() {
    window.dispatchEvent(new CustomEvent('securelife-auth-changed', { detail: currentUser() }));
  }
  function context() {
    try { return JSON.parse(window.localStorage.getItem(contextKey())) || {}; }
    catch (e) { return {}; }
  }
  function saveContext(values) {
    const next = Object.assign({}, context(), values || {});
    window.localStorage.setItem(contextKey(), JSON.stringify(next));
    window.dispatchEvent(new CustomEvent('securelife-context-changed', { detail: next }));
    return next;
  }
  function rememberPolicy(policy) {
    if (!policy || !policy.id) return context();
    const state = context();
    const policies = (state.policies || []).filter((item) => item.id !== policy.id);
    policies.unshift(policy);
    return saveContext({ policies: policies, selectedPolicyId: policy.id });
  }
  async function request(path, options) {
    const settings = Object.assign({ method: 'GET' }, options || {});
    const headers = Object.assign({ Accept: 'application/json' }, settings.headers || {});
    const token = window.localStorage.getItem(TOKEN_KEY);
    if (token) headers.Authorization = 'Bearer ' + token;
    if (settings.body && typeof settings.body !== 'string') {
      headers['Content-Type'] = 'application/json';
      settings.body = JSON.stringify(settings.body);
    }
    settings.headers = headers;
    let response;
    try { response = await fetch(API_BASE + path, settings); }
    catch (e) { throw new Error('The API gateway is not reachable. Start it on port 8080 and try again.'); }
    const text = await response.text();
    let data = null;
    if (text) { try { data = JSON.parse(text); } catch (e) { data = text; } }
    if (!response.ok) {
      const message = data && data.error ? data.error :
        response.status === 401 ? 'Please sign in to continue.' :
        response.status === 403 ? 'Your role cannot perform this action.' :
        response.status === 422 ? 'This record cannot be accepted yet. Check the related user, policy, product, or KYC status.' :
        'Request failed (' + response.status + ').';
      const error = new Error(message);
      error.status = response.status;
      throw error;
    }
    return data;
  }
  async function login(username, password) {
    const result = await request('/users/login', { method: 'POST', body: { username, password } });
    window.localStorage.setItem(TOKEN_KEY, result.token);
    window.localStorage.setItem(USER_KEY, JSON.stringify(result.user));
    notifyAuth();
    return result.user;
  }
  async function syncPolicyholder() {
    const user = currentUser();
    if (!user || user.role !== 'POLICYHOLDER') return context();
    let profile;
    try { profile = await request('/policyholders/by-user/' + user.id); }
    catch (error) {
      if (error.status === 404) return context();
      throw error;
    }
    const results = await Promise.all([
      request('/policyholders/' + profile.id + '/nominees'),
      request('/policyholders/' + profile.id + '/kyc-documents'),
      request('/policyholders/' + profile.id + '/eligibility'),
      request('/policies/policyholder/' + profile.id)
    ]);
    const nominees = results[0] || [], documents = results[1] || [], eligibility = results[2] || {}, policies = results[3] || [];
    const document = documents.length ? documents[documents.length - 1] : null;
    return saveContext({
      policyholderId: profile.id,
      profile: profile,
      nomineeAdded: nominees.length > 0,
      nominee: nominees.length ? nominees[0] : null,
      kycDocumentId: document ? document.id : null,
      kycStatus: eligibility.kycStatus || profile.kycStatus,
      eligible: !!eligibility.eligible,
      policies: policies,
      selectedPolicyId: policies.length ? (context().selectedPolicyId || policies[0].id) : null
    });
  }
  function logout() {
    window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.removeItem(USER_KEY);
    notifyAuth();
  }
  return { baseUrl: API_BASE, request, login, logout, currentUser, context, saveContext, rememberPolicy, syncPolicyholder };
});
