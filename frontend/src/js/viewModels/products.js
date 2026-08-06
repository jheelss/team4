define(['knockout','../accUtils','../services/api'],function(ko,accUtils,api){
  function ProductsViewModel(){
    const user=api.currentUser(); this.isAdmin=ko.observable(user&&user.role==='ADMIN'); this.eligible=ko.observable(!!api.context().eligible);
    this.products=ko.observableArray([]); this.loading=ko.observable(true); this.message=ko.observable(''); this.error=ko.observable(false); this.busy=ko.observable(false);
    this.name=ko.observable('');this.type=ko.observable('TERM');this.coverage=ko.observable();this.premium=ko.observable();this.term=ko.observable();this.description=ko.observable('');this.status=ko.observable('ACTIVE');this.productId=ko.observable();
    this.money=(v)=>new Intl.NumberFormat('en-IN',{style:'currency',currency:'INR',maximumFractionDigits:0}).format(v||0);
    const run=async(work,msg)=>{this.busy(true);this.message('');try{const d=await work();this.message(msg||'');this.error(false);return d;}catch(e){this.message(e.message);this.error(true);throw e;}finally{this.busy(false);}};
    this.load=async()=>{this.loading(true);try{this.products(await api.request('/products'));}catch(e){this.message(e.message);this.error(true);}finally{this.loading(false);}};
    this.choose=(product)=>{api.saveContext({selectedProduct:product,purchaseDraft:true});window.location.href='?ojr=purchase';};
    this.create=async()=>{try{await run(()=>api.request('/products',{method:'POST',body:{productName:this.name(),productType:this.type(),coverageAmount:+this.coverage(),premiumAmount:+this.premium(),policyTerm:+this.term(),description:this.description(),status:this.status()}}),'Product created and added to the catalogue.');await this.load();}catch(e){}};
    this.updateStatus=async()=>{try{await run(()=>api.request('/products/'+this.productId()+'/status?value='+encodeURIComponent(this.status()),{method:'PUT'}),'Product availability updated.');await this.load();}catch(e){}};
    this.connected=()=>{accUtils.announce('Insurance plans loaded.','polite');document.title=(this.isAdmin()?'Product Management':'Insurance Plans')+' - SecureLife';this.load();};
  } return ProductsViewModel;
});
