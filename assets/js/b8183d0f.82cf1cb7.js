"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[685],{5709:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>r,contentTitle:()=>l,default:()=>h,frontMatter:()=>i,metadata:()=>o,toc:()=>c});var a=n(4848),s=n(8453);const i={title:"isolate-ddl-changes"},l=void 0,o={id:"rules/isolate-ddl-changes",title:"isolate-ddl-changes",description:"Why?",source:"@site/docs/rules/isolate-ddl-changes.md",sourceDirName:"rules",slug:"/rules/isolate-ddl-changes",permalink:"/liquibase-linter/docs/rules/isolate-ddl-changes",draft:!1,unlisted:!1,tags:[],version:"current",frontMatter:{title:"isolate-ddl-changes"},sidebar:"docs",previous:{title:"create-index-name",permalink:"/liquibase-linter/docs/rules/index-name"},next:{title:"modify-data-enforce-where",permalink:"/liquibase-linter/docs/rules/modify-data-enforce-where"}},r={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2}];function d(e){const t={code:"code",h2:"h2",p:"p",...(0,s.R)(),...e.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(t.h2,{id:"why",children:"Why?"}),"\n",(0,a.jsx)(t.p,{children:"DDL changes (that is, changes that alter the database structure, rather than modifying data), should be separated from all other changes, such that they have a changeSet all to themselves."}),"\n",(0,a.jsx)(t.p,{children:"Unlike DML (insert, update, delete) changes, DDL changes cannot be rolled back if there is a failure. This means that if a changeSet contains several changes including DDL changes, and one change fails, the database could be left in a part-done state in respect of that changeSet, and require manual intervention to fix."}),"\n",(0,a.jsxs)(t.p,{children:["The ",(0,a.jsx)(t.code,{children:"isolate-ddl-changes"})," rule will fail if any DDL change is not the only change in its changeSet."]}),"\n",(0,a.jsx)(t.h2,{id:"options",children:"Options"}),"\n",(0,a.jsx)(t.p,{children:"No extra options."})]})}function h(e={}){const{wrapper:t}={...(0,s.R)(),...e.components};return t?(0,a.jsx)(t,{...e,children:(0,a.jsx)(d,{...e})}):d(e)}},8453:(e,t,n)=>{n.d(t,{R:()=>l,x:()=>o});var a=n(6540);const s={},i=a.createContext(s);function l(e){const t=a.useContext(i);return a.useMemo((function(){return"function"==typeof e?e(t):{...t,...e}}),[t,e])}function o(e){let t;return t=e.disableParentContext?"function"==typeof e.components?e.components(s):e.components||s:l(e.components),a.createElement(i.Provider,{value:t},e.children)}}}]);