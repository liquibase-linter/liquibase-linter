"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[93],{2071:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>a,contentTitle:()=>i,default:()=>d,frontMatter:()=>l,metadata:()=>s,toc:()=>c});var o=t(4848),r=t(8453);const l={title:"drop-not-null-require-column-data-type"},i=void 0,s={id:"rules/drop-not-null-require-column-data-type",title:"drop-not-null-require-column-data-type",description:"Why?",source:"@site/docs/rules/drop-not-null-require-column-data-type.md",sourceDirName:"rules",slug:"/rules/drop-not-null-require-column-data-type",permalink:"/liquibase-linter/docs/rules/drop-not-null-require-column-data-type",draft:!1,unlisted:!1,tags:[],version:"current",frontMatter:{title:"drop-not-null-require-column-data-type"},sidebar:"docs",previous:{title:"create-table-remarks",permalink:"/liquibase-linter/docs/rules/create-table-remarks"},next:{title:"file-name-no-spaces",permalink:"/liquibase-linter/docs/rules/file-name-no-spaces"}},a={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2}];function u(e){const n={a:"a",code:"code",h2:"h2",p:"p",...(0,r.R)(),...e.components};return(0,o.jsxs)(o.Fragment,{children:[(0,o.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,o.jsxs)(n.p,{children:["When ",(0,o.jsx)(n.a,{href:"http://www.liquibase.org/documentation/changes/drop_not_null_constraint.html",children:"dropping a not-null constraint"}),", some database vendors will fail if the ",(0,o.jsx)(n.code,{children:"columnDataType"})," is not specified, while others will work fine. This can lead to problems if not caught soon enough."]}),"\n",(0,o.jsxs)(n.p,{children:["This rule will fail if a ",(0,o.jsx)(n.code,{children:"dropNotNullConstaint"})," is used without a ",(0,o.jsx)(n.code,{children:"columnDataType"}),"."]}),"\n",(0,o.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,o.jsx)(n.p,{children:"No extra options."})]})}function d(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,o.jsx)(n,{...e,children:(0,o.jsx)(u,{...e})}):u(e)}},8453:(e,n,t)=>{t.d(n,{R:()=>i,x:()=>s});var o=t(6540);const r={},l=o.createContext(r);function i(e){const n=o.useContext(l);return o.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function s(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(r):e.components||r:i(e.components),o.createElement(l.Provider,{value:n},e.children)}}}]);