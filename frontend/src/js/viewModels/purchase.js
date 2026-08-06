define(['knockout','../accUtils','../services/api'],function(ko,accUtils,api){
  function PurchaseViewModel(){
    const state=api.context(),today=new Date();
    this.selected=ko.observable(state.purchaseDraft?state.selectedProduct:null);
    this.holderId=ko.observable(state.policyholderId||'');
    this.issueDate=ko.observable(today.toISOString().slice(0,10));
    this.expiryDate=ko.observable('');
    this.message=ko.observable('');this.error=ko.observable(false);this.busy=ko.observable(false);
    this.money=(v)=>new Intl.NumberFormat('en-IN',{style:'currency',currency:'INR',maximumFractionDigits:0}).format(v||0);
    this.expiryText=ko.pureComputed(()=>{const p=this.selected();if(!p)return '';const d=new Date();d.setFullYear(d.getFullYear()+(p.policyTerm||1));this.expiryDate(d.toISOString().slice(0,10));return d.toLocaleDateString('en-IN',{day:'numeric',month:'short',year:'numeric'});});
    this.submit=async()=>{const p=this.selected();if(!p)return;this.busy(true);this.message('');try{const policy=await api.request('/policies',{method:'POST',body:{policyholderId:+this.holderId(),productId:p.id,issueDate:this.issueDate(),expiryDate:this.expiryDate(),sumAssured:+p.coverageAmount}});api.rememberPolicy(policy);api.saveContext({selectedProduct:null,purchaseDraft:false});window.location.href='?ojr=policies';}catch(e){this.message(e.message);this.error(true);}finally{this.busy(false);}};
    this.cancel=()=>{api.saveContext({selectedProduct:null,purchaseDraft:false});return true;};
    this.connected=()=>{accUtils.announce('Purchase review loaded.','polite');document.title='Purchase Review - SecureLife';if(!this.selected())window.location.href='?ojr=products';};
  }return PurchaseViewModel;
});
