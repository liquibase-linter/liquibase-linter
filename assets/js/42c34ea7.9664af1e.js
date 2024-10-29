"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[418],{8881:(e,t,i)=>{i.r(t),i.d(t,{assets:()=>u,contentTitle:()=>o,default:()=>h,frontMatter:()=>a,metadata:()=>l,toc:()=>c});var n=i(4848),s=i(8453),r=i(6025);const a={title:"New Features in Liquibase Linter 0.3.0",author:"David Goss",authorURL:"http://davidgoss.co/"},o=void 0,l={permalink:"/liquibase-linter/blog/2018/11/30/new-features-in-030",source:"@site/blog/2018-11-30-new-features-in-030.md",title:"New Features in Liquibase Linter 0.3.0",description:"Liquibase Linter 0.3.0 is released and available now in Maven Central!",date:"2018-11-30T00:00:00.000Z",tags:[],readingTime:1.965,hasTruncateMarker:!0,authors:[{name:"David Goss",url:"http://davidgoss.co/",key:null,page:null}],frontMatter:{title:"New Features in Liquibase Linter 0.3.0",author:"David Goss",authorURL:"http://davidgoss.co/"},unlisted:!1,prevItem:{title:"New Features in Liquibase Linter 0.4.0",permalink:"/liquibase-linter/blog/2019/06/21/new-features-in-040"},nextItem:{title:"Introducing Liquibase Linter",permalink:"/liquibase-linter/blog/2018/09/10/introducing-liquibase-linter"}},u={authorsImageUrls:[void 0]},c=[{value:"New core rules",id:"new-core-rules",level:2},{value:"JSON and YAML support",id:"json-and-yaml-support",level:2},{value:"Console reporting and <code>fail-fast</code>",id:"console-reporting-and-fail-fast",level:2},{value:"Custom rules",id:"custom-rules",level:2},{value:"Next",id:"next",level:2}];function d(e){const t={a:"a",code:"code",em:"em",h2:"h2",li:"li",p:"p",ul:"ul",...(0,s.R)(),...e.components};return(0,n.jsxs)(n.Fragment,{children:[(0,n.jsxs)(t.p,{children:["Liquibase Linter ",(0,n.jsx)(t.a,{href:"https://github.com/whiteclarkegroup/liquibase-linter/releases/tag/0.3.0",children:"0.3.0 is released"})," and available now ",(0,n.jsx)(t.a,{href:"https://search.maven.org/artifact/com.whiteclarkegroup/liquibase-linter/0.3.0/jar",children:"in Maven Central"}),"!"]}),"\n",(0,n.jsx)(t.p,{children:"In this release we have fixed a few issues, but primarily we've been restructuring the codebase and adding new features including improved logging and custom rules support."}),"\n",(0,n.jsx)(t.h2,{id:"new-core-rules",children:"New core rules"}),"\n",(0,n.jsxs)(t.ul,{children:["\n",(0,n.jsxs)(t.li,{children:["The ",(0,n.jsxs)(t.a,{href:"../../../../docs/rules/no-schema-name",children:["new ",(0,n.jsx)(t.code,{children:"no-schema-name"})," rule"]})," will prevent changes that use the ",(0,n.jsx)(t.code,{children:"schemaName"})," attribute. This supports the practise we follow internally, where we run Liquibase once per schema with a user who only has access to that schema."]}),"\n"]}),"\n",(0,n.jsx)(t.h2,{id:"json-and-yaml-support",children:"JSON and YAML support"}),"\n",(0,n.jsx)(t.p,{children:"You can now use Liquibase Linter with scripts written in JSON or YAML, as well as XML."}),"\n",(0,n.jsxs)(t.h2,{id:"console-reporting-and-fail-fast",children:["Console reporting and ",(0,n.jsx)(t.code,{children:"fail-fast"})]}),"\n",(0,n.jsx)(t.p,{children:"Previously, we would exit the Liquibase process as soon as the first rule failed. This was not ideal if there were several script issues, as you would need to fix each one in order to find the next --- this made it especially tedious to try to retrofit the linter to an existing project."}),"\n",(0,n.jsxs)(t.p,{children:["Now, we allow ",(0,n.jsx)(t.em,{children:"all"})," changes to be checked, collecting failures as we go and reporting them in a readable way in the console at the end:"]}),"\n",(0,n.jsx)("img",{alt:"Example console output for failed rules",src:(0,r.Ay)("img/console-example.png")}),"\n",(0,n.jsx)(t.p,{children:"(Note that we still don't allow any script to be run if there is a failure; the linter hooks into the parsing phase of Liquibase's lifecycle, and with failures we force the process to exit before it even starts generating SQL.)"}),"\n",(0,n.jsx)(t.h2,{id:"custom-rules",children:"Custom rules"}),"\n",(0,n.jsxs)(t.p,{children:["You can now write your own rule that's specific to your project or company and use it in Liquibase Linter. There's a ",(0,n.jsx)(t.a,{href:"../../../../docs/custom-rules",children:"complete guide in the docs"}),", but essentially you just need to write a Java class and do a little configuration in your project."]}),"\n",(0,n.jsx)(t.p,{children:"This is the change we're most excited about, as it will give users the power to extend the linter to solve their own particular problems with relative ease."}),"\n",(0,n.jsx)(t.p,{children:"In support of this, we've also refactored the codebase quite heavily so that all of our core rules are implemented using the same mechanism as custom rules, so any additional power that gets added to rules in future will benefit both."}),"\n",(0,n.jsx)(t.h2,{id:"next",children:"Next"}),"\n",(0,n.jsx)(t.p,{children:"Here are some things we've got lined up for the next couple of releases:"}),"\n",(0,n.jsxs)(t.ul,{children:["\n",(0,n.jsx)(t.li,{children:"Improving the core suite of rules"}),"\n",(0,n.jsx)(t.li,{children:"Support for running with Gradle"}),"\n",(0,n.jsx)(t.li,{children:"Flexible reporting e.g. JUnit, HTML"}),"\n"]})]})}function h(e={}){const{wrapper:t}={...(0,s.R)(),...e.components};return t?(0,n.jsx)(t,{...e,children:(0,n.jsx)(d,{...e})}):d(e)}},8453:(e,t,i)=>{i.d(t,{R:()=>a,x:()=>o});var n=i(6540);const s={},r=n.createContext(s);function a(e){const t=n.useContext(r);return n.useMemo((function(){return"function"==typeof e?e(t):{...t,...e}}),[t,e])}function o(e){let t;return t=e.disableParentContext?"function"==typeof e.components?e.components(s):e.components||s:a(e.components),n.createElement(r.Provider,{value:t},e.children)}}}]);