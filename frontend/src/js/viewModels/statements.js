define(['knockout','../accUtils','../services/api'],function(ko,accUtils,api){
 function StatementsViewModel(){const state=api.context(),user=api.currentUser();this.policies=ko.observableArray(state.policies||[]);this.policyId=ko.observable(state.selectedPolicyId||(this.policies()[0]&&this.policies()[0].id));this.generatedBy=ko.observable(user?user.fullName:'SYSTEM');this.statementId=ko.observable();this.statement=ko.observable(null);this.message=ko.observable('');this.error=ko.observable(false);this.busy=ko.observable(false);this.money=(v)=>new Intl.NumberFormat('en-IN',{style:'currency',currency:'INR',maximumFractionDigits:0}).format(v||0);
 const run=async(work,msg)=>{this.busy(true);this.message('');try{const d=await work();this.statement(d);this.statementId(d.id);this.message(msg);this.error(false);return d;}catch(e){this.message(e.message);this.error(true);}finally{this.busy(false);}};
 this.generate=()=>run(()=>api.request('/statements/policy/'+this.policyId()+'?generatedBy='+encodeURIComponent(this.generatedBy()),{method:'POST'}),'Statement generated from the latest policy activity.');
 this.find=()=>run(()=>api.request('/statements/'+this.statementId()),'Statement loaded.');
 this.connected=()=>{accUtils.announce('Statements page loaded.','polite');document.title='Statements - SecureLife';};}return StatementsViewModel;
});
