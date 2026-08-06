/**
 * @license
 * Copyright (c) 2014, 2026, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your application specific code will go here
 */
define(['knockout', 'ojs/ojcontext', 'ojs/ojmodule-element-utils', 'ojs/ojknockouttemplateutils', 'ojs/ojcorerouter', 'ojs/ojmodulerouter-adapter', 'ojs/ojknockoutrouteradapter', 'ojs/ojurlparamadapter', 'ojs/ojresponsiveutils', 'ojs/ojresponsiveknockoututils', 'ojs/ojarraydataprovider', './services/api',
        'ojs/ojdrawerpopup', 'ojs/ojmodule-element', 'ojs/ojknockout'],
  function(ko, Context, moduleUtils, KnockoutTemplateUtils, CoreRouter, ModuleRouterAdapter, KnockoutRouterAdapter, UrlParamAdapter, ResponsiveUtils, ResponsiveKnockoutUtils, ArrayDataProvider, api) {

     function ControllerViewModel() {

      this.KnockoutTemplateUtils = KnockoutTemplateUtils;

      // Handle announcements sent when pages change, for Accessibility.
      this.manner = ko.observable('polite');
      this.message = ko.observable();
      announcementHandler = (event) => {
          this.message(event.detail.message);
          this.manner(event.detail.manner);
      };

      document.getElementById('globalBody').addEventListener('announce', announcementHandler, false);


      // Media queries for responsive layouts
      const smQuery = ResponsiveUtils.getFrameworkQuery(ResponsiveUtils.FRAMEWORK_QUERY_KEY.SM_ONLY);
      this.smScreen = ResponsiveKnockoutUtils.createMediaQueryObservable(smQuery);
      const mdQuery = ResponsiveUtils.getFrameworkQuery(ResponsiveUtils.FRAMEWORK_QUERY_KEY.MD_UP);
      this.mdScreen = ResponsiveKnockoutUtils.createMediaQueryObservable(mdQuery);

      const allNav = [
        { path: '', redirect: 'dashboard' },
        { path: 'dashboard', detail: { label: 'Home', iconClass: 'oj-ux-ico-home' } },
        { path: 'customers', detail: { label: 'Onboarding', iconClass: 'oj-ux-ico-contact-group' } },
        { path: 'products', detail: { label: 'Plans', iconClass: 'oj-ux-ico-wallet' } },
        { path: 'purchase', detail: { label: 'Purchase Review', iconClass: 'oj-ux-ico-check' } },
        { path: 'policies', detail: { label: 'My Policies', iconClass: 'oj-ux-ico-file-text' } },
        { path: 'payments', detail: { label: 'Premiums', iconClass: 'oj-ux-ico-credit-card' } },
        { path: 'claims', detail: { label: 'Claims', iconClass: 'oj-ux-ico-warning' } },
        { path: 'statements', detail: { label: 'Statements', iconClass: 'oj-ux-ico-report' } }
      ];

      // Router setup
      let router = new CoreRouter(allNav, {
        urlAdapter: new UrlParamAdapter()
      });
      router.sync();

      this.moduleAdapter = new ModuleRouterAdapter(router);

      this.selection = new KnockoutRouterAdapter(router);

      // Setup the navDataProvider with the routes, excluding the first redirected
      // route.
      this.navDataProvider = ko.observable();
      const updateNavigation = (user) => {
        let paths = ['dashboard'];
        if (user && user.role === 'POLICYHOLDER') paths = ['dashboard','customers','products','policies','payments','claims','statements'];
        if (user && user.role === 'ADMIN') paths = ['dashboard','customers','products','policies','claims'];
        if (user && user.role === 'UNDERWRITER') paths = ['dashboard','customers','policies'];
        if (user && user.role === 'CLAIMS_OFFICER') paths = ['dashboard','claims'];
        const labels = user && user.role !== 'POLICYHOLDER' ? { customers:'KYC Operations', products:'Product Management', policies:'Policy Issuance' } : {};
        const items = allNav.slice(1).filter((item) => paths.indexOf(item.path) >= 0).map((item) => ({ path:item.path, detail:Object.assign({}, item.detail, labels[item.path] ? {label:labels[item.path]} : {}) }));
        this.navDataProvider(new ArrayDataProvider(items, {keyAttributes:'path'}));
      };

      // Drawer
      this.sideDrawerOn = ko.observable(false);

      // Close drawer on medium and larger screens
      this.mdScreen.subscribe(() => { this.sideDrawerOn(false) });

      // Called by navigation drawer toggle button and after selection of nav drawer item
      this.toggleDrawer = () => {
        this.sideDrawerOn(!this.sideDrawerOn());
      }

      // Header
      // Application Name used in Branding Area
      this.appName = ko.observable("SecureLife");
      // User Info used in Global Navigation area
      this.userLogin = ko.observable(api.currentUser() ? api.currentUser().fullName : 'Guest');
      this.userRole = ko.observable(api.currentUser() ? api.currentUser().role : 'SIGN IN');
      updateNavigation(api.currentUser());
      window.addEventListener('securelife-auth-changed', (event) => {
        const user = event.detail;
        this.userLogin(user ? user.fullName : 'Guest');
        this.userRole(user ? user.role : 'SIGN IN');
        updateNavigation(user);
      });
      this.userMenuAction = (event) => {
        if (event.detail.selectedValue === 'out') {
          api.logout();
          router.go({ path: 'dashboard' });
        }
      };

      // Footer
      this.footerLinks = [];
     }
     // release the application bootstrap busy state
     Context.getPageContext().getBusyContext().applicationBootstrapComplete();

     return new ControllerViewModel();
  }
);
