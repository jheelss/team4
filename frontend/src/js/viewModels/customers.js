define(['knockout','../accUtils','../services/api'], function(ko,accUtils,api){
  function CustomersViewModel(){
    const user=api.currentUser(), saved=api.context(), today=new Date().toISOString().slice(0,10);
    this.user=ko.observable(user); this.isCustomer=ko.observable(user&&user.role==='POLICYHOLDER');
    this.firstName=ko.observable(user&&user.fullName?(user.fullName.split(' ')[0]):''); this.lastName=ko.observable(user&&user.fullName?(user.fullName.split(' ').slice(1).join(' ')):'');
    this.dob=ko.observable(''); this.email=ko.observable(user?user.email:''); this.phone=ko.observable(''); this.address=ko.observable('');
    this.holderId=ko.observable(saved.policyholderId||''); this.nomineeName=ko.observable(''); this.relationship=ko.observable('SPOUSE'); this.nomineeDob=ko.observable(''); this.contactNo=ko.observable('');
    this.documentType=ko.observable('AADHAAR'); this.documentNumber=ko.observable(''); this.uploadDate=ko.observable(today);
    this.documentId=ko.observable(saved.kycDocumentId||''); this.verificationStatus=ko.observable('VERIFIED');
    this.context=ko.observable(saved); this.message=ko.observable(''); this.error=ko.observable(false); this.busy=ko.observable(false);
    this.pendingDocuments=ko.observableArray([]);
    this.step=ko.pureComputed(()=>{const s=this.context();return !s.policyholderId?1:!s.nomineeAdded?2:!s.kycDocumentId?3:4;});
    const run=async(work,success)=>{this.busy(true);this.message('');try{const data=await work();this.message(success);this.error(false);return data;}catch(e){this.message(e.message);this.error(true);throw e;}finally{this.busy(false);}};
    const update=(v)=>{const state=api.saveContext(v);this.context(state);return state;};
    this.create=async()=>{try{const d=await run(()=>api.request('/policyholders',{method:'POST',body:{userId:user.id,firstName:this.firstName(),lastName:this.lastName(),dob:this.dob(),email:this.email(),phone:this.phone(),address:this.address(),kycStatus:'PENDING'}}),'Profile complete. Now add a nominee.');this.holderId(d.id);update({policyholderId:d.id,profile:d});}catch(e){}};
    this.addNominee=async()=>{try{const d=await run(()=>api.request('/policyholders/'+this.holderId()+'/nominees',{method:'POST',body:{nomineeName:this.nomineeName(),relationship:this.relationship(),dob:this.nomineeDob(),contactNo:this.contactNo()}}),'Nominee added. Your KYC is the final submission step.');update({nomineeAdded:true,nominee:d});}catch(e){}};
    this.addDocument=async()=>{try{const d=await run(()=>api.request('/policyholders/'+this.holderId()+'/kyc-documents',{method:'POST',body:{documentType:this.documentType(),documentNumber:this.documentNumber(),uploadDate:this.uploadDate(),verificationStatus:'PENDING'}}),'KYC submitted. Verification is now pending.');this.documentId(d.id);update({kycDocumentId:d.id,kycStatus:'PENDING',eligible:false});}catch(e){}};
    this.eligibility=async()=>{try{const d=await run(()=>api.request('/policyholders/'+this.holderId()+'/eligibility'), 'Eligibility refreshed.');update({kycStatus:d.kycStatus,eligible:d.eligible});}catch(e){}};
    this.loadHolder=async()=>{try{const d=await run(()=>api.request('/policyholders/'+this.holderId()),'Customer record loaded.');update({reviewProfile:d});}catch(e){}};
    this.verify=async()=>{try{const d=await run(()=>api.request('/policyholders/'+this.holderId()+'/kyc-documents/'+this.documentId()+'/verification-status?value='+encodeURIComponent(this.verificationStatus()),{method:'PUT'}),'KYC decision saved.');update({reviewDocument:d});}catch(e){}};
    this.loadPending=async()=>{this.busy(true);try{const docs=await api.request('/policyholders/kyc-documents/pending');const rows=await Promise.all((docs||[]).map(async d=>{try{const h=await api.request('/policyholders/'+d.policyholderId);return Object.assign({},d,{holderName:h.firstName+' '+h.lastName,email:h.email});}catch(e){return d;}}));this.pendingDocuments(rows);}catch(e){this.message(e.message);this.error(true);}finally{this.busy(false);}};
    this.decideKyc=async(document,status)=>{try{await run(()=>api.request('/policyholders/'+document.policyholderId+'/kyc-documents/'+document.id+'/verification-status?value='+status,{method:'PUT'}),status==='VERIFIED'?'KYC verified. The customer is now eligible.':'KYC document rejected.');await this.loadPending();}catch(e){}};
    this.connected=async()=>{
      accUtils.announce('Onboarding page loaded.','polite');document.title=(this.isCustomer()?'Onboarding':'KYC Operations')+' - SecureLife';
      if(this.isCustomer()){
        try{const state=await api.syncPolicyholder();this.context(state);this.holderId(state.policyholderId||'');this.documentId(state.kycDocumentId||'');}
        catch(e){this.message(e.message);this.error(true);}
      }else await this.loadPending();
    };
  } return CustomersViewModel;
});
