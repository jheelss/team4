define(['knockout','../accUtils','../services/api'],function(ko,accUtils,api){
  function PoliciesViewModel(){
    const user=api.currentUser(),state=api.context();
    this.isCustomer=ko.observable(user&&user.role==='POLICYHOLDER');
    this.policies=ko.observableArray(state.policies||[]);this.pendingPolicies=ko.observableArray([]);
    this.message=ko.observable('');this.error=ko.observable(false);this.busy=ko.observable(false);
    this.money=(v)=>new Intl.NumberFormat('en-IN',{style:'currency',currency:'INR',maximumFractionDigits:0}).format(v||0);
    this.statusLabel=(status)=>({PENDING_APPROVAL:'Awaiting Approval',ACTIVE:'Active Policy',REJECTED:'Request Rejected'}[status]||status);
    this.statusClass=(status)=>status==='ACTIVE'?'badge success':status==='PENDING_APPROVAL'?'badge warning':'badge neutral';
    this.openPolicy=(policy,route)=>{api.saveContext({selectedPolicyId:policy.id});window.location.href='?ojr='+route;};
    this.loadPending=async()=>{this.busy(true);try{this.pendingPolicies(await api.request('/policies/pending'));}catch(e){this.message(e.message);this.error(true);}finally{this.busy(false);}};
    this.decide=async(policy,status)=>{this.busy(true);this.message('');try{await api.request('/policies/'+policy.id+'/approval?status='+status,{method:'PUT'});this.message(status==='ACTIVE'?'Policy approved and activated.':'Policy request rejected.');await this.loadPending();}catch(e){this.message(e.message);this.error(true);}finally{this.busy(false);}};
    this.connected=async()=>{accUtils.announce('Policies page loaded.','polite');document.title=(this.isCustomer()?'My Policies':'Policy Approvals')+' - SecureLife';if(this.isCustomer()){try{const current=await api.syncPolicyholder();this.policies(current.policies||[]);}catch(e){this.message(e.message);this.error(true);}}else await this.loadPending();};
  }return PoliciesViewModel;
});
