define(['knockout', '../accUtils', '../services/api'], function (ko, accUtils, api) {
  function DashboardViewModel() {
    this.user = ko.observable(api.currentUser());
    this.username = ko.observable(''); this.password = ko.observable('');
    this.fullName = ko.observable(''); this.email = ko.observable('');
    this.registerUsername = ko.observable(''); this.registerPassword = ko.observable('');
    this.message = ko.observable(''); this.error = ko.observable(false); this.busy = ko.observable(false);
    this.context = ko.observable(api.context());
    this.isPolicyholder = ko.pureComputed(() => this.user() && this.user().role === 'POLICYHOLDER');
    this.isStaff = ko.pureComputed(() => this.user() && this.user().role !== 'POLICYHOLDER');
    this.firstName = ko.pureComputed(() => this.user() ? (this.user().fullName || this.user().username).split(' ')[0] : 'there');
    this.onboardingLabel = ko.pureComputed(() => {
      const state = this.context();
      if (!state.policyholderId) return 'Profile not started';
      if (!state.nomineeAdded) return 'Nominee required';
      if (!state.kycDocumentId) return 'KYC required';
      return state.eligible ? 'Eligible to Purchase' : 'Awaiting Verification';
    });
    this.onboardingClass = ko.pureComputed(() => this.context().eligible ? 'badge success' : 'badge warning');
    window.addEventListener('securelife-auth-changed', (event) => { this.user(event.detail); this.context(api.context()); });
    window.addEventListener('securelife-context-changed', (event) => this.context(event.detail));
    const report = (message, error) => { this.message(message); this.error(!!error); };
    this.login = async () => {
      this.busy(true); report('', false);
      try { this.user(await api.login(this.username(), this.password())); this.context(await api.syncPolicyholder()); report('Welcome back. Your account is ready.', false); }
      catch (e) { report(e.message, true); } finally { this.busy(false); }
    };
    this.register = async () => {
      this.busy(true); report('', false);
      try {
        await api.request('/users', { method:'POST', body:{ username:this.registerUsername(), password:this.registerPassword(), fullName:this.fullName(), email:this.email(), role:'POLICYHOLDER' } });
        this.username(this.registerUsername()); this.password(this.registerPassword());
        this.user(await api.login(this.username(), this.password())); this.context(await api.syncPolicyholder()); report('Account created. Complete your profile to continue.', false);
      } catch (e) { report(e.message, true); } finally { this.busy(false); }
    };
    this.connected = async () => {
      accUtils.announce('SecureLife home loaded.', 'polite'); document.title = 'Home - SecureLife';
      if (this.isPolicyholder()) {
        try { this.context(await api.syncPolicyholder()); }
        catch (e) { report(e.message, true); }
      }
    };
  }
  return DashboardViewModel;
});
